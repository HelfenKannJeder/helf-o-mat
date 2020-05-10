package de.helfenkannjeder.helfomat.core.picture

import org.springframework.data.annotation.AccessType
import java.io.Serializable
import java.util.*
import javax.persistence.Column
import javax.persistence.Embeddable

/**
 * @author Valentin Zickner
 */
@Embeddable
@AccessType(AccessType.Type.FIELD)
data class PictureId (
    @Column(name = "pictureId") val value: UUID = UUID.randomUUID()
) : Serializable {

    @Suppress("unused")
    constructor(value: String) : this(UUID.fromString(value))

}