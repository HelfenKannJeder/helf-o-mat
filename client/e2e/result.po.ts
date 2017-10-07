import {by, element} from 'protractor';

export class ResultPage {

    hasMap() {
        return element
            .all((by.css('helfomat-map')))
            .count()
            .then(amount => amount === 1);
    }

    hasResultList() {
        return element
            .all((by.css('helfomat-list')))
            .count()
            .then(amount => amount === 1);
    }

}