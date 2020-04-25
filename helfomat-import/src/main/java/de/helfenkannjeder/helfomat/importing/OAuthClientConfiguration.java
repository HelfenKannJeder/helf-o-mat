package de.helfenkannjeder.helfomat.importing;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.web.client.RestTemplate;

@Configuration
public class OAuthClientConfiguration {

    @Bean
    @ConfigurationProperties("security.oauth2.client")
    public ClientCredentialsResourceDetails clientCredentialsResourceDetails() {
        return new ClientCredentialsResourceDetails();
    }

    @Bean
    public RestTemplate oAuth2RestTemplate(OAuth2ProtectedResourceDetails clientCredentialsResourceDetails) {
        OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(clientCredentialsResourceDetails);
        oAuth2RestTemplate.getMessageConverters().add(new ResourceHttpMessageConverter());
        return oAuth2RestTemplate;
    }

}
