import {Component, Input} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';

@Component({
    selector: 'time',
    template: '{{format()}}'
})
export class TimeComponent {

    @Input()
    private seconds: number;

    constructor(private translateService: TranslateService) {
    }

    public format(): string {
        if (this.seconds == null) {
            return "-";
        }
        let minutesComplete = Math.round(this.seconds / 60);
        let hours = Math.floor(minutesComplete / 60);
        let minutes = minutesComplete % 60;

        return this.translateService.instant('organization.facts.distance.time', {
            hours,
            minutes: TimeComponent.twoDigest(minutes)
        });
    }

    private static twoDigest(number: number): string {
        if (number < 10) {
            return '0' + number;
        }
        return '' + number;
    }

}