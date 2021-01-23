package de.helfenkannjeder.helfomat.infrastructure.email

import de.helfenkannjeder.helfomat.api.EmailService
import de.helfenkannjeder.helfomat.core.ProfileRegistry
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import java.util.*

@Service
@Profile("!${ProfileRegistry.ENABLE_EMAIL}")
class LogEmailService : EmailService {

    override fun sendEmail(to: String, templatePrefix: String, subjectAttributes: Array<Any>, attributes: Map<String, Any?>, attachments: List<Triple<String, Resource, String>>, locale: Locale, replyTo: String?) {
        LOG.info("Send email to '${to}' with template '${templatePrefix}', subjectAttributes: '${subjectAttributes}',  attributes: '${attributes}")
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(LogEmailService::class.java)
    }
}
