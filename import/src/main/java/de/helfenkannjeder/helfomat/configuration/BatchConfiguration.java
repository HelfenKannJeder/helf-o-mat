package de.helfenkannjeder.helfomat.configuration;

import de.helfenkannjeder.helfomat.batch.CreateIndexBatchlet;
import de.helfenkannjeder.helfomat.batch.RenameAliasBatchlet;
import de.helfenkannjeder.helfomat.batch.RenamePictureSymlinkBatchlet;
import de.helfenkannjeder.helfomat.domain.Organisation;
import de.helfenkannjeder.helfomat.processor.AnswerQuestionsProcessor;
import de.helfenkannjeder.helfomat.processor.DuplicateOrganisationFilterProcessor;
import de.helfenkannjeder.helfomat.typo3.domain.TOrganisation;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.jsr.step.batchlet.BatchletAdapter;
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

    private final AnswerQuestionsProcessor answerQuestionsProcessor;

    public BatchConfiguration(AnswerQuestionsProcessor answerQuestionsProcessor) {
        this.answerQuestionsProcessor = answerQuestionsProcessor;
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
            .processor(tOrganisation -> {
                Organisation organisation = organisationProcessor.process(tOrganisation);
                return this.answerQuestionsProcessor.process(organisation);
            })
            .writer(organisationItemWriter)
            .build();
    }

    @Bean
    @Qualifier("importOrganisationFromThw")
    public Step importOrganisationFromThw(StepBuilderFactory stepBuilderFactory,
                                          ItemReader<Organisation> organisationItemReader,
                                          DuplicateOrganisationFilterProcessor duplicateOrganisationFilterProcessor,
                                          ItemWriter<Organisation> organisationItemWriter) {
        return stepBuilderFactory.get("importOrganisationFromThw")
            .listener(new WaitChunkListener(60 * 1000))
            .<Organisation, Organisation>chunk(20)
            .reader(organisationItemReader)
            .processor(organisation -> {
                organisation = duplicateOrganisationFilterProcessor.process(organisation);
                return this.answerQuestionsProcessor.process(organisation);
            })
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
    public Step renamePictureSymlinkStep(StepBuilderFactory stepBuilderFactory,
                                         RenamePictureSymlinkBatchlet renamePictureSymlinkBatchlet) {
        return stepBuilderFactory.get("renamePictureSymlinkStep")
            .tasklet(new BatchletAdapter(renamePictureSymlinkBatchlet))
            .build();
    }


    @Bean
    public Job importDataJob(JobBuilderFactory jobBuilderFactory,
                             Step createIndexStep,
                             @Qualifier("importOrganisationFromJpa") Step importOrganisationFromJpa,
                             @Qualifier("importOrganisationFromThw") Step importOrganisationFromThw,
                             Step renameAliasStep,
                             Step renamePictureSymlinkStep) {
        return jobBuilderFactory.get("importDataJob")
            .start(createIndexStep)
            .next(importOrganisationFromJpa)
            .next(importOrganisationFromThw)
            .next(renameAliasStep)
            .next(renamePictureSymlinkStep)
            .build();
    }
}
