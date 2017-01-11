package de.helfenkannjeder.helfomat.dto;

import de.helfenkannjeder.helfomat.domain.GeoPoint;

/**
 * @author Valentin Zickner
 */
public class GeoPointDto {
    private double lat;
    private double lon;

    public GeoPointDto() {
    }

    private GeoPointDto(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public static GeoPointDto fromGeoPoint(GeoPoint geoPoint) {
        return new GeoPointDto(geoPoint.getLat(), geoPoint.getLon());
    }

    public static GeoPoint toGeoPoint(GeoPointDto geoPointDto) {
        return new GeoPoint(geoPointDto.getLat(), geoPointDto.getLon());
    }
}
