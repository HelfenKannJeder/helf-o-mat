package de.helfenkannjeder.helfomat.core.geopoint

import java.util.*
import kotlin.math.roundToLong

/**
 * @author Valentin Zickner
 */
data class GeoPoint(
    val lat: Double,
    val lon: Double
) {

    fun distanceInKm(second: GeoPoint): Double {
        val R_earth = 6371.0
        val dLat = deg2rad(second.lat - this.lat)
        val dLon = deg2rad(second.lon - this.lon)
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
            Math.cos(deg2rad(this.lat)) * Math.cos(deg2rad(second.lat)) *
            Math.sin(dLon / 2) * Math.sin(dLon / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return R_earth * c
    }

    private fun deg2rad(deg: Double): Double {
        return deg * (Math.PI / 180)
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) {
            return false;
        }
        if (!(other is GeoPoint)) {
            return false;
        }

        return compare(lat, other.lat) && compare(lon, other.lon)
    }

    private fun compare(double1: Double, double2: Double): Boolean =
        toComparableLong(double1) == toComparableLong(double2)

    override fun hashCode(): Int = Objects.hash(
        toComparableLong(lat),
        toComparableLong(lon)
    )

    private fun toComparableLong(double1: Double) = (double1 * 10000000000).roundToLong()
}