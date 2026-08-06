package de.helfenkannjeder.helfomat.api.contact

data class CreateGeneralContactRequestDto(
    val captcha: String,
    val name: String,
    val email: String,
    val subject: String,
    val message: String,
    val location: String?,
    val address: String?,

    /**
     * Anything but an empty value means the
     * submission came from some automation that filled in every input it could find.
     */
    val website: String? = null
)
