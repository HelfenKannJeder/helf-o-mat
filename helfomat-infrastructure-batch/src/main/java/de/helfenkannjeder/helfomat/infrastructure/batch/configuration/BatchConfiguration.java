package de.helfenkannjeder.helfomat.infrastructure.batch.configuration;

import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEvent;
import de.helfenkannjeder.helfomat.infrastructure.batch.batchlet.CreateIndexBatchlet;
import de.helfenkannjeder.helfomat.infrastructure.batch.batchlet.RenameAliasBatchlet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.SimpleJobBuilder;
import org.springframework.batch.core.jsr.step.batchlet.BatchletAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import javax.batch.api.AbstractBatchlet;
import java.util.List;

/**
 * @author Valentin Zickner
 */
@Configuration
@EnableScheduling
public class BatchConfiguration {

    @Component
    class TestListener {

        @EventListener
        public void testEventListener(OrganisationEvent organisationEvent) {
            System.out.println("Received test event");
            System.out.println(organisationEvent);
        }
    }

    @Bean
    public Step createIndexStep(StepBuilderFactory stepBuilderFactory,
                                CreateIndexBatchlet createIndexBatchlet) {
        return stepBuilderFactory.get("createIndexStep")
            .tasklet(new BatchletAdapter(createIndexBatchlet))
            .build();
    }

    @Bean
    public Step noopStep(StepBuilderFactory stepBuilderFactory) {
        return stepBuilderFactory.get("noopStep")
            .tasklet(new BatchletAdapter(new AbstractBatchlet() {
                @Override
                public String process() {
                    return null;
                }
            }))
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
                             Step noopStep,
                             @Qualifier("importSteps") List<Step> importSteps) {
        SimpleJobBuilder importDataJob = jobBuilderFactory.get("importDataJob")
            .start(noopStep);
        for (Step importStep : importSteps) {
            importDataJob = importDataJob.next(importStep);
        }
        return importDataJob
            .build();
    }
}
