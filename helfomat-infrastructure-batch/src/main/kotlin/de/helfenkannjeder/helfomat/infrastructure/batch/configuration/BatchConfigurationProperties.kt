package de.helfenkannjeder.helfomat.infrastructure.batch.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("helfomat.batch")
data class BatchConfigurationProperties(
    val errorNotificationEmail: String
)