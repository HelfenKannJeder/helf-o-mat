import GeoPoint from "./geopoint.model";

export default class Address {
    public street: string;
    public addressAppendix: string;
    public city: string;
    public zipcode: string;
    public location: GeoPoint;
    public telephone: string;
    public website: string;
}