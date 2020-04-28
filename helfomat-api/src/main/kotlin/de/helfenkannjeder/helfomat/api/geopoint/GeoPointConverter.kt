package de.helfenkannjeder.helfomat.api.geopoint

import de.helfenkannjeder.helfomat.core.geopoint.GeoPoint
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

/**
 * @author Valentin Zickner
 */
@Component
class GeoPointConverter : Converter<String, GeoPoint> {

    override fun convert(string: String): GeoPoint? {
        val geoPointParts = string.split(",")
        return if (geoPointParts.size == 2) {
            GeoPoint(geoPointParts[0].toDouble(), geoPointParts[1].toDouble())
        } else {
            null
        }
    }

}