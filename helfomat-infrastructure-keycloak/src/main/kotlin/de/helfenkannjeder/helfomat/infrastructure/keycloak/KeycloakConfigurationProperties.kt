package de.helfenkannjeder.helfomat.infrastructure.keycloak

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("keycloak")
data class KeycloakConfigurationProperties(
    val realm: String,
    val resource: String,
    val authServerUrl: String,
    val sslRequired: String,
    val publicClient: String,
    val principalAttribute: String
)