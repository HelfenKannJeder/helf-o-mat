package de.helfenkannjeder.helfomat.infrastructure.keycloak

import de.helfenkannjeder.helfomat.core.user.User
import de.helfenkannjeder.helfomat.core.user.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Repository
import org.springframework.web.client.RestTemplate

@Repository
@EnableConfigurationProperties(KeycloakConfigurationProperties::class)
open class KeycloakUserRepository(
    val keycloakRestTemplate: RestTemplate,
    val keycloakConfigurationProperties: KeycloakConfigurationProperties
) : UserRepository {

    override fun findByUsername(username: String): User? {
        var response = keycloakRestTemplate.exchange(
            "${keycloakConfigurationProperties.authServerUrl}/admin/realms/${keycloakConfigurationProperties.realm}/users",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<List<KeycloakUserSearchResponseDto>>() {}
        )
        LOGGER.info(
            "retrieved user info for username={} with responseStatusCode={} bodyToString={}",
            username,
            response.statusCodeValue,
            response.body.toString()
        )

        return response
            .body
            ?.firstOrNull { it.username == username }
            ?.toUser()
    }

    companion object {
        private var LOGGER = LoggerFactory.getLogger(KeycloakUserRepository::class.java)
    }

}

fun KeycloakUserSearchResponseDto.toUser() = User(this.username, this.firstName, this.lastName, this.email)