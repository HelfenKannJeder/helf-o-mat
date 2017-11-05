import {GeoPoint} from '../app/organisation/geopoint.model';

export const environment = {
    offline: true,
    production: false,
    defaults: {
        mapCenter: new GeoPoint(51.163375, 10.447683),
        zoomLevel: {
            withoutPosition: 6,
            withPosition: 12
        }
    }
};
