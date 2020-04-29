package de.helfenkannjeder.helfomat.core.organization

import de.helfenkannjeder.helfomat.core.picture.PictureId

/**
 * @author Valentin Zickner
 */
data class Volunteer(
    var firstname: String,
    var lastname: String? = null,
    var motivation: String,
    var picture: PictureId? = null
)