import {GeoPoint} from "./geopoint";
import {AuthConfig} from "angular-oauth2-oidc";

export interface Environment {
    offline: boolean;
    production: boolean;
    kiosk: boolean;
    readonly: boolean;
    qrCodeUrl: string;
    defaults: {
        countries: string[];
        mapCenter: GeoPoint;
        zoomLevel: {
            withoutPosition: number;
            withPosition: number;
        },
        kiosk: {
            showCountdownAfter: number;
            navigateToHomeAfter: number;
        }
    },
    useHttps: boolean;
    auth: AuthConfig;
}
