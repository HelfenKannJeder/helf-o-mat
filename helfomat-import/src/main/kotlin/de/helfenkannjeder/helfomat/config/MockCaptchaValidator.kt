package de.helfenkannjeder.helfomat.config

import de.helfenkannjeder.helfomat.api.CaptchaValidator
import org.springframework.stereotype.Service

@Service
class MockCaptchaValidator : CaptchaValidator {

    override fun validate(captcha: String): Boolean = true

}