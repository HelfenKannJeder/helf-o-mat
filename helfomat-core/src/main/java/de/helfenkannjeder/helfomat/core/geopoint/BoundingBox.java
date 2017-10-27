package de.helfenkannjeder.helfomat.core.geopoint;

public class BoundingBox {
    private GeoPoint northEast;
    private GeoPoint southWest;

    BoundingBox() {
    }

    public BoundingBox(GeoPoint northEast, GeoPoint southWest) {
        this.northEast = northEast;
        this.southWest = southWest;
    }

    public GeoPoint getNorthEast() {
        return northEast;
    }

    public GeoPoint getSouthWest() {
        return southWest;
    }

}
