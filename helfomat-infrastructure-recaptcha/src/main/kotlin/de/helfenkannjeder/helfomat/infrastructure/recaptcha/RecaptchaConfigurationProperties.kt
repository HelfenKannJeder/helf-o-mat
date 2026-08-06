package de.helfenkannjeder.helfomat.infrastructure.recaptcha

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("helfomat.recaptcha")
class RecaptchaConfigurationProperties(
    val endpoint: String = "https://www.google.com/recaptcha/api/siteverify",
    val secret: String,

    /**
     * reCAPTCHA v3 scores every interaction between 0.0 (very likely a bot) and 1.0 (very likely a
     * human). Google recommends 0.5 as a starting point; lower it if legitimate submissions are
     * rejected, raise it if spam still gets through.
     */
    val minimumScore: Double = 0.5,

    /**
     * Domains a token is accepted from, checked against the hostname reCAPTCHA reports back. Empty
     * disables the check, which is what local development, the tests and the kiosk/offline setups
     * rely on.
     */
    val allowedHostnames: List<String> = emptyList()
)
