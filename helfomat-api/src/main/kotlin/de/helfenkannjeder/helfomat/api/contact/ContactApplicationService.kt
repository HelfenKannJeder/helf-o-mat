package de.helfenkannjeder.helfomat.api.contact

import de.helfenkannjeder.helfomat.api.CaptchaValidator
import de.helfenkannjeder.helfomat.api.EmailService
import de.helfenkannjeder.helfomat.api.randomString
import de.helfenkannjeder.helfomat.api.ratelimit.RateLimiter
import de.helfenkannjeder.helfomat.core.contact.ContactRequest
import de.helfenkannjeder.helfomat.core.contact.ContactRequestId
import de.helfenkannjeder.helfomat.core.contact.ContactRequestRepository
import de.helfenkannjeder.helfomat.core.contact.ContactRequestStatus
import de.helfenkannjeder.helfomat.core.organization.Organization
import de.helfenkannjeder.helfomat.core.organization.OrganizationRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.core.io.ClassPathResource
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * @author Valentin Zickner
 */
@Service
@Transactional
@EnableConfigurationProperties(ContactFormRateLimitProperties::class)
open class ContactApplicationService(
    val organizationRepository: OrganizationRepository,
    val contactRequestRepository: ContactRequestRepository,
    val captchaValidator: CaptchaValidator,
    val emailService: EmailService,
    val rateLimiter: RateLimiter,
    val rateLimitProperties: ContactFormRateLimitProperties,
    @Value("\${helfomat.contact-form.domain:https://www.helfenkannjeder.de/}") val domain: String,
    @Value("\${helfomat.contact-form.locale:de_DE}") val locale: String,
    @Value("\${helfomat.contact-form.force-to:}") val contactFormForceTo: String,
    @Value("\${helfomat.contact-form.support-email}") val supportEmail: String
) {

    open fun createContactRequest(contactRequestDto: CreateContactRequestDto, clientIp: String): ContactRequestResult {
        if (isBlocked("contact-request", contactRequestDto.website, contactRequestDto.captcha, SUBMIT_ACTION, clientIp, contactRequestDto.email)) {
            // Nothing is stored and no mail is sent. The caller still gets an id back so a bot
            // cannot tell a dropped submission from an accepted one; the follow up calls for that
            // id then fail the same way they would for an expired request.
            return ContactRequestResult(ContactRequestId())
        }

        val organization = organizationRepository.findOne(contactRequestDto.organizationId.value) ?: throw ContactRequestInvalid()
        val contactRequest = contactRequestDto.toContactRequest(randomString(25), organization)
        sendConfirmationEmail(contactRequest, organization)
        contactRequest.markConfirmationAsSent()
        return contactRequestRepository.save(contactRequest).toContactRequestResult()
    }

    open fun resendContactRequest(resendContactRequestDto: ResendContactRequestDto, clientIp: String): ContactRequestResult {
        if (isBlocked("contact-request-resend", null, resendContactRequestDto.captcha, RESUBMIT_ACTION, clientIp, null)) {
            return ContactRequestResult(resendContactRequestDto.contactRequestId)
        }

        // An id that was never stored is what a dropped submission handed out, so it is answered
        // like a resend that worked instead of with an error.
        val contactRequest = contactRequestRepository.findById(resendContactRequestDto.contactRequestId).orElse(null)
            ?: return ContactRequestResult(resendContactRequestDto.contactRequestId)
        val organization = organizationRepository.findOne(contactRequest.organizationId.value) ?: throw ContactRequestInvalid()

        if (contactRequest.numberOfConfirmationEmails > 3 || contactRequest.status != ContactRequestStatus.CONFIRMATION_REQUEST_SENT) {
            throw MaxContactRequestReached()
        }

        sendConfirmationEmail(contactRequest, organization)
        contactRequest.markConfirmationAsSent()
        return contactRequestRepository.save(contactRequest).toContactRequestResult()
    }

    open fun confirmContactRequest(confirmContactRequestDto: ConfirmContactRequestDto): ConfirmContactRequestResult {
        try {
            val contactRequest = this.contactRequestRepository.getOne(confirmContactRequestDto.contactRequestId)
            if (contactRequest.confirmationCode != confirmContactRequestDto.confirmationCode) {
                throw ContactRequestInvalid()
            }
            if (contactRequest.status != ContactRequestStatus.CONFIRMATION_REQUEST_SENT) {
                throw ContactRequestInvalid()
            }
            val organization = this.organizationRepository.findOne(contactRequest.organizationId.value) ?: throw ContactRequestInvalid()

            val attributes = mapOf(
                Pair("domain", domain),
                Pair("contactRequest", contactRequest),
                Pair("organization", organization)
            )
            val attachments = listOf(
                Triple("logo", ClassPathResource("templates/logo.jpg"), "image/jpeg")
            )
            var to = contactRequest.contactPerson.email
            if (contactFormForceTo != "") {
                to = contactFormForceTo
            }
            emailService.sendEmail(to, "contact-request-send-email", arrayOf(contactRequest.subject), attributes, attachments, toLocale(), contactRequest.email)

            contactRequest.status = ContactRequestStatus.EMAIL_CONFIRMED
            this.contactRequestRepository.save(contactRequest)
            return ConfirmContactRequestResult(
                contactRequest.organizationId,
                organization.urlName
            )
        } catch (e: JpaObjectRetrievalFailureException) {
            throw ContactRequestInvalid()
        }
    }

    open fun getById(contactRequestId: ContactRequestId): ConfirmContactRequestResult {
        val contactRequest = this.contactRequestRepository.getOne(contactRequestId)
        val organization = this.organizationRepository.findOne(contactRequest.organizationId.value) ?: throw ContactRequestInvalid()
        return ConfirmContactRequestResult(
            contactRequest.organizationId,
            organization.urlName
        )
    }

    open fun createGeneralContactRequest(generalContactRequestDto: CreateGeneralContactRequestDto, clientIp: String) {
        if (isBlocked("contact-form", generalContactRequestDto.website, generalContactRequestDto.captcha, SUBMIT_ACTION, clientIp, generalContactRequestDto.email)) {
            return
        }

        val attributes = mapOf(
            Pair("domain", domain),
            Pair("contactRequest", generalContactRequestDto)
        )

        emailService.sendEmail(
            supportEmail,
            "general-contact-request-send-email",
            arrayOf(generalContactRequestDto.subject),
            attributes,
            listOf(),
            toLocale(),
            generalContactRequestDto.email
        )

    }

    /**
     * Decides whether a submission has to be dropped.
     *
     * Every check answers the caller as if the submission had gone through. Bots probe forms to
     * find out which ones deliver, and an error would both confirm that the endpoint is alive and
     * point at the check that stopped them.
     */
    private fun isBlocked(form: String, honeypot: String?, captcha: String, expectedAction: String, clientIp: String, email: String?): Boolean {
        if (!honeypot.isNullOrBlank()) {
            LOGGER.warn("Dropped {} from {}, the honeypot field was filled in", form, clientIp)
            return true
        }

        if (!captchaValidator.validate(captcha, expectedAction)) {
            LOGGER.warn("Dropped {} from {}, captcha validation failed", form, clientIp)
            return true
        }

        if (!rateLimiter.tryConsume("$form:ip:$clientIp", rateLimitProperties.perIpLimit, rateLimitProperties.perIpWindow)) {
            LOGGER.warn(
                "Dropped {} from {}, more than {} submissions within {}",
                form, clientIp, rateLimitProperties.perIpLimit, rateLimitProperties.perIpWindow
            )
            return true
        }

        if (email != null && !rateLimiter.tryConsume("$form:email:${email.toMailboxKey()}", rateLimitProperties.perEmailLimit, rateLimitProperties.perEmailWindow)) {
            LOGGER.warn(
                "Dropped {} from {}, more than {} submissions for the same mailbox within {}",
                form, clientIp, rateLimitProperties.perEmailLimit, rateLimitProperties.perEmailWindow
            )
            return true
        }

        return false
    }

    private fun toLocale(): Locale {
        val localeParts = locale.split("_")
        return when (localeParts.size) {
            2 -> Locale(localeParts[0], localeParts[1])
            else -> Locale.getDefault()
        }
    }

    private fun sendConfirmationEmail(contactRequest: ContactRequest, organization: Organization) {
        val attributes = mapOf(
            Pair("domain", domain),
            Pair("contactRequest", contactRequest),
            Pair("organization", organization)
        )
        val attachments = listOf(
            Triple("logo", ClassPathResource("templates/logo.jpg"), "image/jpeg")
        )
        emailService.sendEmail(contactRequest.email, "contact-request-confirmation-email", arrayOf(contactRequest.subject), attributes, attachments, toLocale(), null)
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(ContactApplicationService::class.java)

        private const val SUBMIT_ACTION = "submit"
        private const val RESUBMIT_ACTION = "resubmit"
    }

}
