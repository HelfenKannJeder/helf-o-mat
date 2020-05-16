package de.helfenkannjeder.helfomat.infrastructure.azure

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("azure.storage")
data class AzureConfigurationProperties(
    val endpoint: String,
    val connectionString: String,
    val containerName: String
)