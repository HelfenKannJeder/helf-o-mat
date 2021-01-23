package de.helfenkannjeder.helfomat.infrastructure.batch.configuration

import de.helfenkannjeder.helfomat.infrastructure.batch.listener.ErrorNotificationListener
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.jsr.step.batchlet.BatchletAdapter
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.batch.api.AbstractBatchlet

/**
 * @author Valentin Zickner
 */
@Configuration
open class BatchConfiguration {

    @Bean
    open fun noopStep(stepBuilderFactory: StepBuilderFactory): Step {
        return stepBuilderFactory["noopStep"]
            .tasklet(BatchletAdapter(object : AbstractBatchlet() {
                override fun process(): String? {
                    return null
                }
            }))
            .build()
    }

    @Bean
    open fun importDataJob(
        jobBuilderFactory: JobBuilderFactory,
        noopStep: Step,
        @Qualifier("importSteps") importSteps: List<Step>,
        emailNotificationListener: ErrorNotificationListener
    ): Job {
        val importDataJob = jobBuilderFactory["importDataJob"].start(noopStep)
        importSteps.forEach { importDataJob.next(it) }
        return importDataJob
            .listener(emailNotificationListener)
            .build()
    }

}