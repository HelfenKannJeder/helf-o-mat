package de.helfenkannjeder.helfomat.core.picture

import java.io.Serializable
import java.util.*

/**
 * @author Valentin Zickner
 */
data class PictureId (
    val value: String = UUID.randomUUID().toString()
) : Serializable