package de.helfenkannjeder.helfomat.core.geopoint;

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
}
