import {GeoPoint} from '../_internal/geopoint';
import {Environment} from "../_internal/environment.interface";

export const environment: Environment = {
    offline: true,
    production: false,
    kiosk: false,
    readonly: false,
    qrCodeUrl: '',
    defaults: {
        countries: ['de'],
        mapCenter: new GeoPoint(51.163375, 10.447683),
        zoomLevel: {
            withoutPosition: 6,
            withPosition: 12
        }
    },
    useHttps: false,
    auth: {
        issuer: '/auth/realms/helfenkannjeder',
        clientId: 'helfomat-web-ui',
        scope: 'profile email',
        responseType: 'code',
        // at_hash is not present in JWT token
        disableAtHashCheck: true,
        showDebugInformation: true
    }
};

export {AlternativeMapModule as MapImplementationModule} from '../app/map/alternative/alternative-map.module';