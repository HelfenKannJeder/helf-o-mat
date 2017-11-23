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

    public static double distanceInKm(GeoPoint first, GeoPoint second) {
        double R_earth = 6371;
        double dLat = deg2rad(second.getLat() - first.getLat());
        double dLon = deg2rad(second.getLon() - first.getLon());
        double a =
            Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(deg2rad(first.getLat())) * Math.cos(deg2rad(second.getLat())) *
                    Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R_earth * c;
    }

    private static double deg2rad(double deg) {
        return deg * (Math.PI / 180);
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
