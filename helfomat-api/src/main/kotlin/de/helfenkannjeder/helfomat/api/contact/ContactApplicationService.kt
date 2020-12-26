package de.helfenkannjeder.helfomat.api.contact

import de.helfenkannjeder.helfomat.api.CaptchaValidationFailedException
import de.helfenkannjeder.helfomat.api.CaptchaValidator
import de.helfenkannjeder.helfomat.api.EmailService
import de.helfenkannjeder.helfomat.api.randomString
import de.helfenkannjeder.helfomat.core.contact.ContactRequestRepository
import de.helfenkannjeder.helfomat.core.organization.OrganizationRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @author Valentin Zickner
 */
@Service
@Transactional
open class ContactApplicationService(
    val organizationRepository: OrganizationRepository,
    val contactRequestRepository: ContactRequestRepository,
    val captchaValidator: CaptchaValidator,
    val emailService: EmailService
) {

    open fun createContactRequest(contactRequestDto: CreateContactRequestDto) {
        if (!this.captchaValidator.validate(contactRequestDto.captcha)) {
            throw CaptchaValidationFailedException()
        }

        val organization = organizationRepository.findOne(contactRequestDto.organizationId.value) ?: throw ContactRequestInvalid()
        val contactRequest = contactRequestDto.toContactRequest(randomString(25), organization)
        val attributes = mapOf(
            Pair("contactRequest", contactRequest),
            Pair("organization", organization)
        )
        emailService.sendEmail(contactRequest.email, "contact-request-confirmation-email", attributes)
        contactRequest.markConfirmationAsSent()
        contactRequestRepository.save(contactRequest)
    }

}