package de.helfenkannjeder.helfomat.core.organization

import de.helfenkannjeder.helfomat.core.geopoint.GeoPoint

/**
 * @author Valentin Zickner
 */
data class Address(
    val street: String,
    val addressAppendix: String? = null,
    val city: String,
    val zipcode: String,
    val location: GeoPoint,
    val telephone: String? = null,
    val website: String? = null
)