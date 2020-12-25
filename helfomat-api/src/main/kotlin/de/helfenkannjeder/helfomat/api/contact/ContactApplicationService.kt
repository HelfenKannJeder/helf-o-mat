package de.helfenkannjeder.helfomat.api.contact

import de.helfenkannjeder.helfomat.api.CaptchaValidationFailedException
import de.helfenkannjeder.helfomat.api.CaptchaValidator
import org.springframework.stereotype.Service

@Service
open class ContactApplicationService(
    val captchaValidator: CaptchaValidator
) {

    fun createContactRequest(contactRequestDto: CreateContactRequestDto) {
        if (!this.captchaValidator.validate(contactRequestDto.captcha)) {
            throw CaptchaValidationFailedException()
        }
        println("Contact request: $contactRequestDto")
    }

}