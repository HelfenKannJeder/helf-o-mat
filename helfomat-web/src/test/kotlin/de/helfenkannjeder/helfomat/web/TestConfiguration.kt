package de.helfenkannjeder.helfomat.web

import de.helfenkannjeder.helfomat.core.organization.OrganizationRepository
import org.elasticsearch.client.Client
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.jwt.JwtDecoder

@Configuration
open class TestConfiguration {
    @MockBean
    var client: Client? = null

    @MockBean
    var organizationRepository: OrganizationRepository? = null

    @MockBean
    var jwtDecoder: JwtDecoder? = null
}