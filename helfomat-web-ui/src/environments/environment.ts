// The file contents for the current environment will overwrite these during build.
// The build system defaults to the dev environment which uses `environment.ts`, but if you do
// `ng build --env=prod` then `environment.prod.ts` will be used instead.
// The list of which env maps to which file can be found in `angular-cli.json`.

import {GeoPoint} from '../app/organisation/geopoint.model';

export const environment = {
    offline: false,
    production: false,
    defaults: {
        mapCenter: new GeoPoint(51.163375, 10.447683),
        zoomLevel: {
            withoutPosition: 6,
            withPosition: 12
        }
    }
};
