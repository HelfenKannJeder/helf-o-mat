package de.helfenkannjeder.helfomat.api.search;

/**
 * @author Valentin Zickner
 */
public class ClusteredGeoPointDto {
    private GeoPointDto geoPoint;
    private long count;

    public ClusteredGeoPointDto(GeoPointDto geoPoint, long count) {
        this.geoPoint = geoPoint;
        this.count = count;
    }

    public GeoPointDto getGeoPoint() {
        return geoPoint;
    }

    public long getCount() {
        return count;
    }

}
