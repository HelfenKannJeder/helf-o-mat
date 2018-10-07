package de.helfenkannjeder.helfomat.web.configuration;

import de.helfenkannjeder.helfomat.core.IndexManager;
import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationReader;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationRepository;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEvent;
import de.helfenkannjeder.helfomat.infrastructure.batch.listener.UniqueOrganisationUrlNameOrganisationProcessor;
import de.helfenkannjeder.helfomat.infrastructure.batch.processor.OrganisationDifferenceProcessor;
import de.helfenkannjeder.helfomat.infrastructure.batch.writer.OrganisationItemWriter;
import de.helfenkannjeder.helfomat.infrastructure.elasticsearch.ElasticsearchConfiguration;
import de.helfenkannjeder.helfomat.infrastructure.elasticsearch.organisation.ElasticsearchOrganisationRepository;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.util.Pair;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Valentin Zickner
 */
@Configuration
public class ImportBatchConfig {

    @Bean
    @Qualifier("importSteps")
    public List<Step> importSteps(StepBuilderFactory stepBuilderFactory,
                                  List<OrganisationReader> organisationReaders,
                                  ElasticsearchConfiguration elasticsearchConfiguration,
                                  ElasticsearchTemplate elasticsearchTemplate,
                                  ApplicationEventPublisher applicationEventPublisher,
                                  OrganisationRepository organisationRepository,
                                  @Value("classpath:/mapping/organisation.json") Resource organisationMapping) {
        return organisationReaders.stream()
            .map((OrganisationReader organisationReader) -> {
                UniqueOrganisationUrlNameOrganisationProcessor uniqueOrganisationUrlNameOrganisationProcessor = new UniqueOrganisationUrlNameOrganisationProcessor();
                OrganisationStepExecutionListener organisationStepExecutionListener = new OrganisationStepExecutionListener(organisationReader, elasticsearchConfiguration, elasticsearchTemplate, organisationMapping, organisationRepository);
                return stepBuilderFactory.get("import" + organisationReader.getClass().getSimpleName())
                    .<Organisation, Pair<Organisation, Stream<OrganisationEvent>>>chunk(20)
                    .reader(organisationReader::read)
                    .processor((Function<Organisation, Pair<Organisation, Stream<OrganisationEvent>>>) organisation -> {
                        organisation = uniqueOrganisationUrlNameOrganisationProcessor.apply(organisation);
                        return organisationStepExecutionListener.getOrganisationDifferenceProcessor().process(organisation);
                    })
                    .writer((List<? extends Pair<Organisation, Stream<OrganisationEvent>>> organisationInfo) -> {
                        organisationInfo.stream().flatMap(Pair::getSecond).forEach(applicationEventPublisher::publishEvent);
                        organisationStepExecutionListener.getOrganisationItemWriter().write(organisationInfo.stream().map(Pair::getFirst).collect(Collectors.toList()));
                    })
                    .listener(organisationStepExecutionListener)
                    .build();
            })
            .collect(Collectors.toList());
    }

    private static class OrganisationStepExecutionListener implements StepExecutionListener {


        private final OrganisationReader organisationReader;
        private final ElasticsearchConfiguration elasticsearchConfiguration;
        private final ElasticsearchTemplate elasticsearchTemplate;
        private final Resource organisationMapping;
        private final OrganisationRepository generalOrganisationRepository;
        private OrganisationDifferenceProcessor organisationDifferenceProcessor;
        private OrganisationItemWriter organisationItemWriter;

        OrganisationStepExecutionListener(OrganisationReader organisationReader, ElasticsearchConfiguration elasticsearchConfiguration, ElasticsearchTemplate elasticsearchTemplate, Resource organisationMapping, OrganisationRepository generalOrganisationRepository) {
            this.organisationReader = organisationReader;
            this.elasticsearchConfiguration = elasticsearchConfiguration;
            this.elasticsearchTemplate = elasticsearchTemplate;
            this.organisationMapping = organisationMapping;
            this.generalOrganisationRepository = generalOrganisationRepository;
        }

        @Override
        public void beforeStep(StepExecution stepExecution) {
            String readerName = organisationReader.getName();
            String index = elasticsearchConfiguration.getIndex() + "-" + readerName;
            OrganisationRepository organisationRepository = new ElasticsearchOrganisationRepository(
                elasticsearchConfiguration,
                elasticsearchTemplate,
                index
            );
            try {
                String mapping = StreamUtils.copyToString(organisationMapping.getInputStream(), Charset.forName("UTF8"));
                organisationRepository.createIndex(mapping);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            organisationDifferenceProcessor = new OrganisationDifferenceProcessor(organisationRepository, generalOrganisationRepository);
            organisationItemWriter = new OrganisationItemWriter(organisationRepository);
        }

        @Override
        public ExitStatus afterStep(StepExecution stepExecution) {
            return null;
        }

        OrganisationDifferenceProcessor getOrganisationDifferenceProcessor() {
            return organisationDifferenceProcessor;
        }

        OrganisationItemWriter getOrganisationItemWriter() {
            return organisationItemWriter;
        }
    }

    @Bean
    @JobScope
    public OrganisationRepository importOrganisationRepository(
        ElasticsearchConfiguration elasticsearchConfiguration,
        ElasticsearchTemplate elasticsearchTemplate,
        IndexManager indexManager
    ) {
        return new ElasticsearchOrganisationRepository(
            elasticsearchConfiguration,
            elasticsearchTemplate,
            indexManager.getCurrentIndex()
        );
    }

}
