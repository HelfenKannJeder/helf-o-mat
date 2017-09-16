export default class GeoPoint {
    public lat: number;
    public lon: number;

    constructor(lat: number, lon: number) {
        this.lat = lat;
        this.lon = lon;
    }

    public static pointBetween(first: GeoPoint, second: GeoPoint) {
        return new GeoPoint(
            (first.lat + second.lat) / 2,
            (first.lon + second.lon) / 2
        )
    }

    public static distanceInKm(first: GeoPoint, second: GeoPoint) {
        const R_earth = 6371;
        const dLat = GeoPoint.deg2rad(second.lat - first.lat);
        const dLon = GeoPoint.deg2rad(second.lon - first.lon);
        const a =
            Math.sin(dLat / 2) * Math.sin(dLat / 2) +
            Math.cos(GeoPoint.deg2rad(first.lat)) * Math.cos(GeoPoint.deg2rad(second.lat)) *
            Math.sin(dLon / 2) * Math.sin(dLon / 2);
        const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R_earth * c;
    }

    private static deg2rad(deg) {
        return deg * (Math.PI / 180)
    }
}