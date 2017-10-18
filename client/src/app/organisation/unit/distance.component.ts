import {Component, Input} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';

@Component({
    selector: 'distance',
    template: '{{format()}}'
})
export class DistanceComponent {

    @Input()
    private meter: number;

    constructor(private translateService: TranslateService) {
    }

    public format(): string {
        if (this.meter >= 1500) {
            let kilometer = this.meter / 1000;
            return this.translateService.instant('organisation.facts.distance.kilometer', {
                kilometers: DistanceComponent.numberToString(Math.round(kilometer * 10) / 10)
            });
        }

        return this.translateService.instant('organisation.facts.distance.meter', {
            meters: this.meter
        });
    }

    private static numberToString(number: number): string {
        let s = '' + number;
        return s.replace('.', ',');
    }

}