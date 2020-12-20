package de.helfenkannjeder.helfomat.infrastructure.keycloak

data class KeycloakUserSearchResponseDto(
    val id: String,
    val createdTimestamp: Long,
    val username: String,
    val enabled: String,
    val emailVerified: String?,
    val firstName: String?,
    val lastName: String?,
    val email: String?
)