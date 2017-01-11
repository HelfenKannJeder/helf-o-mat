import GeoPoint from "./geopoint.model";

export default class BoundingBox {
    public northEast: GeoPoint;
    public southWest: GeoPoint;


    constructor(northEast: GeoPoint, southWest: GeoPoint) {
        this.northEast = northEast;
        this.southWest = southWest;
    }
}