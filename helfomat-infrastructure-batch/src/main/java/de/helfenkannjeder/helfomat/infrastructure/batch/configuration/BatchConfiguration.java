package de.helfenkannjeder.helfomat.infrastructure.batch.configuration;

import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationReader;
import de.helfenkannjeder.helfomat.infrastructure.batch.batchlet.CreateIndexBatchlet;
import de.helfenkannjeder.helfomat.infrastructure.batch.batchlet.RenameAliasBatchlet;
import de.helfenkannjeder.helfomat.infrastructure.batch.batchlet.RenamePictureSymlinkBatchlet;
import de.helfenkannjeder.helfomat.infrastructure.batch.listener.UniqueOrganisationUrlNameOrganisationProcessor;
import de.helfenkannjeder.helfomat.infrastructure.batch.processor.AnswerQuestionsProcessor;
import de.helfenkannjeder.helfomat.infrastructure.batch.processor.DuplicateOrganisationFilterProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.SimpleJobBuilder;
import org.springframework.batch.core.jsr.step.batchlet.BatchletAdapter;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Valentin Zickner
 */
@Configuration
@EnableScheduling
public class BatchConfiguration {

    private final AnswerQuestionsProcessor answerQuestionsProcessor;

    public BatchConfiguration(AnswerQuestionsProcessor answerQuestionsProcessor) {
        this.answerQuestionsProcessor = answerQuestionsProcessor;
    }

    @Bean
    @Qualifier("importSteps")
    public List<Step> importOrganisationFromThw(StepBuilderFactory stepBuilderFactory,
                                                List<OrganisationReader> organisationReaders,
                                                DuplicateOrganisationFilterProcessor duplicateOrganisationFilterProcessor,
                                                UniqueOrganisationUrlNameOrganisationProcessor uniqueOrganisationUrlNameOrganisationProcessor,
                                                ItemWriter<Organisation> organisationItemWriter) {
        return organisationReaders.stream()
            .map(organisationReader ->
                stepBuilderFactory.get("import" + organisationReader.getClass().getSimpleName())
                    .<Organisation, Organisation>chunk(20)
                    .reader(organisationReader::read)
                    .processor(organisation -> {
                        organisation = duplicateOrganisationFilterProcessor.process(organisation);
                        organisation = uniqueOrganisationUrlNameOrganisationProcessor.process(organisation);
                        return this.answerQuestionsProcessor.process(organisation);
                    })
                    .writer(organisationItemWriter)
                    .build()
            )
            .collect(Collectors.toList());
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
                             @Qualifier("importSteps") List<Step> importSteps,
                             Step renameAliasStep,
                             Step renamePictureSymlinkStep) {
        SimpleJobBuilder importDataJob = jobBuilderFactory.get("importDataJob")
            .start(createIndexStep);
        for (Step importStep : importSteps) {
            importDataJob = importDataJob.next(importStep);
        }
        return importDataJob
            .next(renameAliasStep)
            .next(renamePictureSymlinkStep)
            .build();
    }
}
