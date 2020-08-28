package de.helfenkannjeder.helfomat.infrastructure.keycloak

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.ResourceHttpMessageConverter
import org.springframework.security.oauth2.client.OAuth2RestTemplate
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails
import org.springframework.web.client.RestTemplate

@Configuration
open class KeycloakOAuthClientConfiguration {

    @Bean
    @ConfigurationProperties("security.keycloak.client")
    open fun keycloakClientCredentialsResourceDetails(): ClientCredentialsResourceDetails {
        return ClientCredentialsResourceDetails()
    }

    @Bean
    open fun keycloakRestTemplate(keycloakClientCredentialsResourceDetails: OAuth2ProtectedResourceDetails?): RestTemplate {
        val oAuth2RestTemplate = OAuth2RestTemplate(keycloakClientCredentialsResourceDetails)
        oAuth2RestTemplate.messageConverters.add(ResourceHttpMessageConverter())
        return oAuth2RestTemplate
    }

}