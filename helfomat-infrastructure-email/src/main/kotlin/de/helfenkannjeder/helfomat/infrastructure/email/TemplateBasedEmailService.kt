package de.helfenkannjeder.helfomat.infrastructure.email

import de.helfenkannjeder.helfomat.api.EmailService
import de.helfenkannjeder.helfomat.core.ProfileRegistry
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Profile
import org.springframework.core.io.Resource
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import java.util.*
import javax.mail.internet.MimeMessage


/**
 * @author Valentin Zickner
 */
@Service
@EnableConfigurationProperties(EmailConfiguration::class)
@Profile(ProfileRegistry.ENABLE_EMAIL)
class TemplateBasedEmailService(
    val emailConfiguration: EmailConfiguration,
    val emailSender: JavaMailSender,
    val templateEngine: TemplateEngine,
    val messageSource: MessageSource
) : EmailService {

    override fun sendEmail(to: String, templatePrefix: String, subjectAttributes: Array<Any>, attributes: Map<String, Any?>, attachments: List<Triple<String, Resource, String>>, locale: Locale) {
        LOGGER.info("Send email to '${to}' with template '${templatePrefix}' and additional information '${attributes}'")

        val mimeMessage: MimeMessage = this.emailSender.createMimeMessage();
        val message = MimeMessageHelper(mimeMessage, true, "UTF-8")

        val ctx = Context(locale)
        for (attribute in attributes) {
            ctx.setVariable(attribute.key, attribute.value)
        }

        val subject: String = messageSource.getMessage("${templatePrefix}.subject", subjectAttributes, locale)
        message.setSubject(subject)
        message.setFrom(emailConfiguration.sender)
        message.setTo(to)

        val htmlContent: String = this.templateEngine.process("${templatePrefix}-body", ctx)
        message.setText(htmlContent, true)

        for (attachment in attachments) {
            message.addInline(attachment.first, attachment.second, attachment.third)
        }

        emailSender.send(mimeMessage)
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(TemplateBasedEmailService::class.java)
    }

}