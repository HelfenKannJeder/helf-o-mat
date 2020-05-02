package de.helfenkannjeder.helfomat.api.organization

import de.helfenkannjeder.helfomat.core.geopoint.GeoPoint

/**
 * @author Valentin Zickner
 */
data class AddressDto(
    val street: String,
    val addressAppendix: String?,
    val city: String,
    val zipcode: String,
    val location: GeoPoint,
    val telephone: String?,
    val website: String?
)