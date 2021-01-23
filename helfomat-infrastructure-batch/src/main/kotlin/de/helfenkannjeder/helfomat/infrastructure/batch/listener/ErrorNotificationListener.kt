package de.helfenkannjeder.helfomat.infrastructure.batch.listener

import de.helfenkannjeder.helfomat.api.EmailService
import de.helfenkannjeder.helfomat.infrastructure.batch.configuration.BatchConfigurationProperties
import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobExecutionListener
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Component
import java.util.*

@Component
@EnableConfigurationProperties(BatchConfigurationProperties::class)
class ErrorNotificationListener(
    private val batchConfigurationProperties: BatchConfigurationProperties,
    private val emailService: EmailService
) : JobExecutionListener {

    override fun beforeJob(jobExecution: JobExecution) {
    }

    override fun afterJob(jobExecution: JobExecution) {
        if (jobExecution.status != BatchStatus.COMPLETED) {
            emailException(jobExecution.allFailureExceptions.firstOrNull())
        }
    }

    private fun emailException(e: Throwable?) {
        emailService.sendEmail(
            to = batchConfigurationProperties.errorNotificationEmail,
            replyTo = null,
            templatePrefix = "batch-job-error-notification",
            attributes = mapOf(Pair("exception", e?.toString())),
            locale = Locale.getDefault(),
            attachments = emptyList(),
            subjectAttributes = emptyArray()
        )
    }
}