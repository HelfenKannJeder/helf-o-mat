package de.helfenkannjeder.helfomat.api.search;

/**
 * @author Valentin Zickner
 */
public class BoundingBoxDto {
    private GeoPointDto northEast;
    private GeoPointDto southWest;

    public GeoPointDto getNorthEast() {
        return northEast;
    }

    public void setNorthEast(GeoPointDto northEast) {
        this.northEast = northEast;
    }

    public GeoPointDto getSouthWest() {
        return southWest;
    }

    public void setSouthWest(GeoPointDto southWest) {
        this.southWest = southWest;
    }
}
