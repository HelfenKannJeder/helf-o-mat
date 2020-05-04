import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
    name: 'time'
})
export class TimePipe implements PipeTransform {

    transform(value: string): string {
        if (value === undefined || value === null) {
            return null;
        }
        let time = value.split(":");
        return time[0] + ":" + time[1];
    }

}