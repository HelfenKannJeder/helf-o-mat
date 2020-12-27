package de.helfenkannjeder.helfomat.api.contact

import de.helfenkannjeder.helfomat.api.CaptchaValidationFailedException
import de.helfenkannjeder.helfomat.api.CaptchaValidator
import de.helfenkannjeder.helfomat.api.EmailService
import de.helfenkannjeder.helfomat.api.randomString
import de.helfenkannjeder.helfomat.core.contact.ContactRequestRepository
import de.helfenkannjeder.helfomat.core.organization.OrganizationRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * @author Valentin Zickner
 */
@Service
@Transactional
open class ContactApplicationService(
    val organizationRepository: OrganizationRepository,
    val contactRequestRepository: ContactRequestRepository,
    val captchaValidator: CaptchaValidator,
    val emailService: EmailService,
    @Value("\${helfomat.domain:https://www.helfenkannjeder.de/}") val domain: String,
    @Value("\${helfomat.locale:de_DE}") val locale: String
) {

    open fun createContactRequest(contactRequestDto: CreateContactRequestDto) {
        if (!this.captchaValidator.validate(contactRequestDto.captcha)) {
            throw CaptchaValidationFailedException()
        }

        val organization = organizationRepository.findOne(contactRequestDto.organizationId.value) ?: throw ContactRequestInvalid()
        val contactRequest = contactRequestDto.toContactRequest(randomString(25), organization)
        val attributes = mapOf(
            Pair("domain", domain),
            Pair("contactRequest", contactRequest),
            Pair("organization", organization)
        )
        val attachments = listOf(
            Triple("logo", ClassPathResource("templates/logo.jpg"), "image/jpeg")
        )
        val localeParts = locale.split("_")
        val newLocale = when (localeParts.size) {
            2 -> Locale(localeParts[0], localeParts[1])
            else -> Locale.getDefault()
        }
        emailService.sendEmail(contactRequest.email, "contact-request-confirmation-email", arrayOf(contactRequest.subject), attributes, attachments, newLocale)
        contactRequest.markConfirmationAsSent()
        contactRequestRepository.save(contactRequest)
    }

}