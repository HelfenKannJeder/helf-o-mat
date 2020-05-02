package de.helfenkannjeder.helfomat.api.organization

import de.helfenkannjeder.helfomat.core.picture.PictureId

/**
 * @author Valentin Zickner
 */
data class VolunteerDto(
    val firstname: String,
    val motivation: String,
    val picture: PictureId?
)