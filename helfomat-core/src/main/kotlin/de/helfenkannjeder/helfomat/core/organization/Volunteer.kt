package de.helfenkannjeder.helfomat.core.organization

import de.helfenkannjeder.helfomat.core.picture.PictureId

/**
 * @author Valentin Zickner
 */
data class Volunteer(
    var firstname: String? = null,
    var lastname: String? = null,
    var motivation: String? = null,
    var picture: PictureId? = null
) {

    data class Builder(
        var firstname: String? = null,
        var lastname: String? = null,
        var motivation: String? = null,
        var picture: PictureId? = null
    ) {
        fun setFirstname(firstname: String?) = apply { this.firstname = firstname }
        fun setLastname(lastname: String?) = apply { this.lastname = lastname }
        fun setMotivation(motivation: String?) = apply { this.motivation = motivation }
        fun setPicture(picture: PictureId?) = apply { this.picture = picture }

        fun build(): Volunteer {
            return Volunteer(firstname, lastname, motivation, picture)
        }
    }

}