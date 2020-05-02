package de.helfenkannjeder.helfomat.api.organization

import de.helfenkannjeder.helfomat.core.picture.PictureId

/**
 * @author Valentin Zickner
 */
data class ContactPersonDto(
    val firstname: String,
    val lastname: String,
    val rank: String?,
    val telephone: String?,
    val mail: String?,
    val picture: PictureId?
)