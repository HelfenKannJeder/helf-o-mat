import {by, element} from 'protractor';

export class LocationPage {
    hasMap() {
        return element
            .all((by.css('helfomat-map')))
            .count()
            .then(amount => amount === 1);
    }
}