package de.helfenkannjeder.helfomat.importing

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.ResourceHttpMessageConverter
import org.springframework.security.oauth2.client.OAuth2RestTemplate
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails
import org.springframework.web.client.RestTemplate

@Configuration
open class OAuthClientConfiguration {

    @Bean
    @ConfigurationProperties("security.oauth2.client")
    open fun clientCredentialsResourceDetails(): ClientCredentialsResourceDetails {
        return ClientCredentialsResourceDetails()
    }

    @Bean
    open fun oAuth2RestTemplate(clientCredentialsResourceDetails: OAuth2ProtectedResourceDetails?): RestTemplate {
        val oAuth2RestTemplate = OAuth2RestTemplate(clientCredentialsResourceDetails)
        oAuth2RestTemplate.messageConverters.add(ResourceHttpMessageConverter())
        return oAuth2RestTemplate
    }

}