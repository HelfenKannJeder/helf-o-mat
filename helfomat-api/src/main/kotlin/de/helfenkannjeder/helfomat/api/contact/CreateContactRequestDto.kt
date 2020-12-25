package de.helfenkannjeder.helfomat.api.contact

data class CreateContactRequestDto(
    val captcha: String,
    val email: String,
    val subject: String,
    val message: String
)