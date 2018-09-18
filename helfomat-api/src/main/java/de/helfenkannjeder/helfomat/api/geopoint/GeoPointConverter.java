package de.helfenkannjeder.helfomat.api.geopoint;

import de.helfenkannjeder.helfomat.core.geopoint.GeoPoint;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * @author Valentin Zickner
 */
@Component
public class GeoPointConverter implements Converter<String, GeoPoint> {

    @Override
    public GeoPoint convert(String string) {
        if (string == null) {
            return null;
        }
        String[] geoPointParts = string.split(",");
        if (geoPointParts.length == 2) {
            return new GeoPoint(
                Double.parseDouble(geoPointParts[0]),
                Double.parseDouble(geoPointParts[1])
            );
        }
        return null;
    }

}
