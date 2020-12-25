package de.helfenkannjeder.helfomat.infrastructure.recaptcha

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("helfomat.recaptcha")
class RecaptchaConfigurationProperties(
    val endpoint: String = "https://www.google.com/recaptcha/api/siteverify",
    val secret: String
)