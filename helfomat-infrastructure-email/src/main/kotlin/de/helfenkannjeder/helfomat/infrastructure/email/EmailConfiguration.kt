package de.helfenkannjeder.helfomat.infrastructure.email

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("helfomat.email")
data class EmailConfiguration(
    val receiver: String,
    val referencedUrl: String,
    val sender: String
)