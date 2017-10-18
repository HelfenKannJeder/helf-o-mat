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
        let seconds = this.seconds % 60;
        let minutes = Math.floor(this.seconds / 60);

        return this.translateService.instant('organisation.facts.distance.time', {
            minutes,
            seconds: TimeComponent.twoDigest(seconds)
        });
    }

    private static twoDigest(seconds: number): string {
        if (seconds < 10) {
            return '0' + seconds;
        }
        return '' + seconds;
    }

}