package de.helfenkannjeder.helfomat.importing;

import de.helfenkannjeder.helfomat.core.organization.Organization;
import de.helfenkannjeder.helfomat.core.organization.OrganizationReader;
import de.helfenkannjeder.helfomat.core.organization.OrganizationRepository;
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEvent;
import de.helfenkannjeder.helfomat.infrastructure.batch.listener.UniqueOrganizationUrlNameOrganizationProcessor;
import de.helfenkannjeder.helfomat.infrastructure.batch.processor.AnswerQuestionsProcessor;
import de.helfenkannjeder.helfomat.infrastructure.batch.processor.OrganizationDifferenceProcessor;
import de.helfenkannjeder.helfomat.infrastructure.batch.writer.OrganizationItemWriter;
import de.helfenkannjeder.helfomat.infrastructure.elasticsearch.ElasticsearchConfiguration;
import de.helfenkannjeder.helfomat.infrastructure.elasticsearch.organization.ElasticsearchOrganizationRepository;
import de.helfenkannjeder.helfomat.rest.RestOrganizationEventPublisher;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.util.Pair;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
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
                                  List<OrganizationReader> organizationReaders,
                                  AnswerQuestionsProcessor answerQuestionsProcessor,
                                  ElasticsearchConfiguration elasticsearchConfiguration,
                                  ElasticsearchTemplate elasticsearchTemplate,
                                  RestOrganizationEventPublisher restOrganizationEventPublisher,
                                  OrganizationRepository organizationRepository,
                                  @Value("classpath:/mapping/organization.json") Resource organizationMapping,
                                  @Qualifier("legacyTransactionManager") PlatformTransactionManager transactionManager) {
        return organizationReaders.stream()
            .map((OrganizationReader organizationReader) -> {
                UniqueOrganizationUrlNameOrganizationProcessor uniqueOrganizationUrlNameOrganizationProcessor = new UniqueOrganizationUrlNameOrganizationProcessor();
                OrganizationStepExecutionListener organizationStepExecutionListener = new OrganizationStepExecutionListener(organizationReader, elasticsearchConfiguration, elasticsearchTemplate, organizationMapping, organizationRepository);
                return stepBuilderFactory.get("import" + organizationReader.getClass().getSimpleName())
                    .<Organization, Pair<Organization, Stream<OrganizationEvent>>>chunk(20)
                    .reader(organizationReader::read)
                    .processor((Function<Organization, Pair<Organization, Stream<OrganizationEvent>>>) organization -> {
                        organization = uniqueOrganizationUrlNameOrganizationProcessor.apply(organization);
                        organization = answerQuestionsProcessor.process(organization);
                        return organizationStepExecutionListener.getOrganizationDifferenceProcessor().process(organization);
                    })
                    .writer((List<? extends Pair<Organization, Stream<OrganizationEvent>>> organizationInfo) -> {
                        OrganizationItemWriter organizationItemWriter = organizationStepExecutionListener.getOrganizationItemWriter();
                        for (Pair<Organization, Stream<OrganizationEvent>> organizationStreamPair : organizationInfo) {
                            restOrganizationEventPublisher.publishEvents(organizationStreamPair.getFirst(), organizationStreamPair.getSecond());
                            organizationItemWriter.write(Collections.singletonList(organizationStreamPair.getFirst()));
                        }
                    })
                    .listener(organizationStepExecutionListener)
                    .listener(uniqueOrganizationUrlNameOrganizationProcessor)
                    .transactionManager(transactionManager)
                    .build();
            })
            .collect(Collectors.toList());
    }

    private static class OrganizationStepExecutionListener implements StepExecutionListener {


        private final OrganizationReader organizationReader;
        private final ElasticsearchConfiguration elasticsearchConfiguration;
        private final ElasticsearchTemplate elasticsearchTemplate;
        private final Resource organizationMapping;
        private final OrganizationRepository generalOrganizationRepository;
        private OrganizationDifferenceProcessor organizationDifferenceProcessor;
        private OrganizationItemWriter organizationItemWriter;

        OrganizationStepExecutionListener(OrganizationReader organizationReader, ElasticsearchConfiguration elasticsearchConfiguration, ElasticsearchTemplate elasticsearchTemplate, Resource organizationMapping, OrganizationRepository generalOrganizationRepository) {
            this.organizationReader = organizationReader;
            this.elasticsearchConfiguration = elasticsearchConfiguration;
            this.elasticsearchTemplate = elasticsearchTemplate;
            this.organizationMapping = organizationMapping;
            this.generalOrganizationRepository = generalOrganizationRepository;
        }

        @Override
        public void beforeStep(StepExecution stepExecution) {
            String readerName = organizationReader.getName();
            String index = elasticsearchConfiguration.getIndex() + "-" + readerName;
            OrganizationRepository organizationRepository = new ElasticsearchOrganizationRepository(
                elasticsearchConfiguration,
                elasticsearchTemplate,
                index
            );
            try {
                String mapping = StreamUtils.copyToString(organizationMapping.getInputStream(), StandardCharsets.UTF_8);
                organizationRepository.createIndex(mapping);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            organizationDifferenceProcessor = new OrganizationDifferenceProcessor(organizationRepository, generalOrganizationRepository);
            organizationItemWriter = new OrganizationItemWriter(organizationRepository);
        }

        @Override
        public ExitStatus afterStep(StepExecution stepExecution) {
            return null;
        }

        OrganizationDifferenceProcessor getOrganizationDifferenceProcessor() {
            return organizationDifferenceProcessor;
        }

        OrganizationItemWriter getOrganizationItemWriter() {
            return organizationItemWriter;
        }
    }

}
