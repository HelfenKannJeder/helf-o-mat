package de.helfenkannjeder.helfomat.core.organization

import de.helfenkannjeder.helfomat.core.picture.PictureId

/**
 * @author Valentin Zickner
 */
data class ContactPerson(
    val firstname: String,
    val lastname: String? = null,
    val rank: String? = null,
    val telephone: String? = null,
    val mail: String? = null,
    val picture: PictureId? = null
)