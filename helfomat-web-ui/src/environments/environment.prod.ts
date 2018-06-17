import {GeoPoint} from '../_internal/geopoint';

export const environment = {
    offline: false,
    production: true,
    defaults: {
        mapCenter: new GeoPoint(51.163375, 10.447683),
        zoomLevel: {
            withoutPosition: 6,
            withPosition: 12
        }
    }
};
