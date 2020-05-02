package de.helfenkannjeder.helfomat.web

import de.helfenkannjeder.helfomat.core.organization.OrganizationRepository
import org.elasticsearch.client.Client
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.jwt.JwtDecoder

@Configuration
open class TestConfiguration {
    @MockBean
    lateinit var client: Client

    @MockBean
    lateinit var organizationRepository: OrganizationRepository

    @MockBean
    lateinit var jwtDecoder: JwtDecoder
}