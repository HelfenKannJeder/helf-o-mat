// The file contents for the current environment will overwrite these during build.
// The build system defaults to the dev environment which uses `environment.ts`, but if you do
// `ng build --env=prod` then `environment.prod.ts` will be used instead.
// The list of which env maps to which file can be found in `angular-cli.json`.

import {GeoPoint} from '../_internal/geopoint';
import {Environment} from "../_internal/environment.interface";

export const environment: Environment = {
    offline: false,
    production: false,
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
        issuer: '/auth/realms/helfomat',
        clientId: 'helfomat-web-ui',
        scope: 'profile email',
        responseType: 'code',
        // at_hash is not present in JWT token
        disableAtHashCheck: true,
        showDebugInformation: true
    }
};
