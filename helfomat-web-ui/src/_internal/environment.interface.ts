import {GeoPoint} from "./geopoint";
import {AuthConfig} from "angular-oauth2-oidc";

export interface Environment {
    offline: boolean;
    production: boolean;
    defaults: {
        countries: string[];
        mapCenter: GeoPoint;
        zoomLevel: {
            withoutPosition: number;
            withPosition: number;
        }
    },
    useHttps: boolean;
    auth: AuthConfig;
}