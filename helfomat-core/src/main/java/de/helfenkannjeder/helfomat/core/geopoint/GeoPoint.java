package de.helfenkannjeder.helfomat.core.geopoint;

import java.util.Objects;

/**
 * @author Valentin Zickner
 */
public class GeoPoint {
    private double lat;
    private double lon;

    GeoPoint() {
    }

    public GeoPoint(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeoPoint geoPoint = (GeoPoint) o;
        return Double.compare(geoPoint.lat, lat) == 0 &&
            Double.compare(geoPoint.lon, lon) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(lat, lon);
    }

    @Override
    public String toString() {
        return "GeoPoint{" +
            "lat=" + lat +
            ", lon=" + lon +
            '}';
    }
}
