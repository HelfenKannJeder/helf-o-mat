import {GeoPoint} from '../_internal/geopoint';
import {Environment} from "../_internal/environment.interface";

export const environment: Environment = {
    offline: true,
    production: true,
    kiosk: true,
    readonly: true,
    qrCodeUrl: 'https://www.helfenkannjeder.de/kiosk',
    defaults: {
        countries: ['de'],
        mapCenter: new GeoPoint(51.163375, 10.447683),
        zoomLevel: {
            withoutPosition: 6,
            withPosition: 12
        },
        kiosk: {
            showCountdownAfter: 60,
            navigateToHomeAfter: 90
        }
    },
    useHttps: true,
    auth: {
        issuer: '/realms/helfenkannjeder',
        clientId: 'helfomat-web-ui',
        scope: 'openid profile email offline_access',
        responseType: 'code',
        // at_hash is not present in JWT token
        disableAtHashCheck: true,
        showDebugInformation: true
    },
    recaptchaSiteKey: ''
};

export {NoneMapModule as MapImplementationModule} from '../app/map/none/none-map.module';