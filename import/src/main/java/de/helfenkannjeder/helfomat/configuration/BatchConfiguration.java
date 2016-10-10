package de.helfenkannjeder.helfomat.configuration;

import de.helfenkannjeder.helfomat.batch.RenameAliasBatchlet;
import de.helfenkannjeder.helfomat.batch.CreateIndexBatchlet;
import de.helfenkannjeder.helfomat.domain.Organisation;
import de.helfenkannjeder.helfomat.service.ListCache;
import de.helfenkannjeder.helfomat.typo3.domain.TOrganisation;
import de.helfenkannjeder.helfomat.typo3.domain.TQuestion;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.jsr.step.batchlet.BatchletAdapter;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Valentin Zickner
 */
@Configuration
public class BatchConfiguration {

    @Bean
    public Step importQuestionFromJpa(StepBuilderFactory stepBuilderFactory,
                                      ItemReader<TQuestion> questionItemReader,
                                      ListCache<TQuestion> listCache) {
        return stepBuilderFactory.get("importQuestionFromJpa")
                .<TQuestion, TQuestion>chunk(20)
                .reader(questionItemReader)
                .processor(question -> {
                    if (question.getHelfomat() == 3) {
                        return question;
                    } else {
                        return null;
                    }
                })
                .writer(e -> e.forEach(listCache::add))
                .build();
    }

    @Bean
    @Qualifier("importOrganisationFromJpa")
    public Step importOrganisationFromJpa(StepBuilderFactory stepBuilderFactory,
                                          ItemReader<TOrganisation> organisationItemReader,
                                          ItemProcessor<TOrganisation, Organisation> organisationProcessor,
                                          ItemWriter<Organisation> organisationItemWriter) {
        return stepBuilderFactory.get("importOrganisationFromJpa")
                .<TOrganisation, Organisation>chunk(100)
                .reader(organisationItemReader)
                .processor(organisationProcessor)
                .writer(organisationItemWriter)
                .build();
    }

    @Bean
    @Qualifier("importOrganisationFromThw")
    public Step importOrganisationFromThw(StepBuilderFactory stepBuilderFactory,
                                          ItemReader<Organisation> organisationItemReader,
                                          ItemWriter<Organisation> organisationItemWriter) {
        return stepBuilderFactory.get("importOrganisationFromThw")
				.listener(new WaitChunkListener(60*1000))
                .<Organisation, Organisation>chunk(20)
                .reader(organisationItemReader)
                .writer(organisationItemWriter)
                .build();
    }

    @Bean
    public Step createIndexStep(StepBuilderFactory stepBuilderFactory,
                                CreateIndexBatchlet createIndexBatchlet) {
        return stepBuilderFactory.get("createIndexStep")
                .tasklet(new BatchletAdapter(createIndexBatchlet))
                .build();
    }

    @Bean
    public Step renameAliasStep(StepBuilderFactory stepBuilderFactory,
                                RenameAliasBatchlet renameAliasBatchlet) {
        return stepBuilderFactory.get("renameAliasStep")
                .tasklet(new BatchletAdapter(renameAliasBatchlet))
                .build();
    }

    @Bean
    public Job importDataJob(JobBuilderFactory jobBuilderFactory,
                             Step createIndexStep,
                             Step importQuestionFromJpa,
                             @Qualifier("importOrganisationFromJpa") Step importOrganisationFromJpa,
                             @Qualifier("importOrganisationFromThw") Step importOrganisationFromThw,
                             Step renameAliasStep) {
        return jobBuilderFactory.get("importDataJob")
                .start(createIndexStep)
                .next(importQuestionFromJpa)
                .next(importOrganisationFromJpa)
                .next(importOrganisationFromThw)
                .next(renameAliasStep)
                .build();
    }
}
