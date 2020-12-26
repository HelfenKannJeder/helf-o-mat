package de.helfenkannjeder.helfomat.infrastructure.email

import de.helfenkannjeder.helfomat.api.EmailService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

/**
 * @author Valentin Zickner
 */
@Service
class TemplateBasedEmailService : EmailService {

    override fun sendEmail(to: String, templatePrefix: String, attributes: Map<String, Any?>) {
        LOGGER.info("Send email to '${to}' with template '${templatePrefix}' and additional information '${attributes}'")
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(TemplateBasedEmailService.javaClass)
    }

}