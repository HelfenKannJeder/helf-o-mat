package de.helfenkannjeder.helfomat.api

interface CaptchaValidator {

    fun validate(captcha: String): Boolean

}

class CaptchaValidationFailedException : Exception("Failed to validate CAPTCHA")
