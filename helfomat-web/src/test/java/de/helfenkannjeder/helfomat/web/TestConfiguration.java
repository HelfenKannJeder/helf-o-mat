package de.helfenkannjeder.helfomat.web;

import de.helfenkannjeder.helfomat.core.organisation.OrganisationRepository;
import org.elasticsearch.client.Client;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;

@Configuration
public class TestConfiguration {

    @MockBean
    Client client;

    @MockBean
    OrganisationRepository organisationRepository;

    @MockBean
    JwtDecoder jwtDecoder;

}
