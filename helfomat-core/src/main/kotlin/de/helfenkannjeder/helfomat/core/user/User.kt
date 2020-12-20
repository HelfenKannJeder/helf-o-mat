package de.helfenkannjeder.helfomat.core.user

data class User(
    val username: String,
    val firstName: String?,
    val lastName: String?,
    val email: String?
)