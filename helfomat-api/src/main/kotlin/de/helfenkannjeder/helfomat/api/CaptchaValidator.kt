package de.helfenkannjeder.helfomat.api

interface CaptchaValidator {

    /**
     * Checks whether the token belongs to a real user interaction.
     *
     * [expectedAction] has to be the action the token was created with on the client side. Without
     * that check a token obtained on any other page of the site could be replayed against a form
     * that is protected more strictly.
     */
    fun validate(captcha: String, expectedAction: String): Boolean

}

class CaptchaValidationFailedException : Exception("Failed to validate CAPTCHA")
