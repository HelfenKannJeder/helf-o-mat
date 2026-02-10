import {GeoPoint} from '../_internal/geopoint';
import {Environment} from "../_internal/environment.interface";

export const environment: Environment = {
    offline: false,
    production: true,
    kiosk: false,
    readonly: false,
    qrCodeUrl: '',
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
        scope: 'profile email',
        responseType: 'code',
        // at_hash is not present in JWT token
        disableAtHashCheck: true,
        showDebugInformation: true
    },
    recaptchaSiteKey: '6Lc5sxQaAAAAADTPI9Yc-HmxbOj5SBtms3u0h3bV',
    umami: {
        'helfenkannjeder.de': {
            src: 'https://u.helfenkannjeder.de/script.js',
            websiteId: '62fb97cd-e68a-4ee1-8350-68ad75761a50',
        },
        'dev.helf-o-mat.eu': {
            src: 'https://u.dev.helf-o-mat.eu/script.js',
            websiteId: '6721c1c6-dce4-4aee-8edb-ddec24711c80',
        },
    },
};

export {GoogleMapsModule as MapImplementationModule} from '../app/map/google/google-maps.module';