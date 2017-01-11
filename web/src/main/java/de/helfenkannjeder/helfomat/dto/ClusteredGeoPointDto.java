package de.helfenkannjeder.helfomat.dto;

import de.helfenkannjeder.helfomat.domain.GeoPoint;

/**
 * @author Valentin Zickner
 */
public class ClusteredGeoPointDto {
    private GeoPoint geoPoint;
    private long count;

    public ClusteredGeoPointDto(GeoPoint geoPoint, long count) {
        this.geoPoint = geoPoint;
        this.count = count;
    }

    public GeoPoint getGeoPoint() {
        return geoPoint;
    }

    public long getCount() {
        return count;
    }

}
