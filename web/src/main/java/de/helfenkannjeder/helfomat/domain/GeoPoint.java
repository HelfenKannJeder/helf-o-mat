package de.helfenkannjeder.helfomat.domain;

/**
 * @author Valentin Zickner
 */
public class GeoPoint {
    private double lat;
    private double lon;

    public GeoPoint() {
    }

    public GeoPoint(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public static GeoPoint fromGeoPoint(org.elasticsearch.common.geo.GeoPoint geoPoint) {
        return new GeoPoint(geoPoint.lat(), geoPoint.lon());
    }
}
