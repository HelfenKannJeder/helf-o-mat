import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
    name: 'time'
})
export class TimePipe implements PipeTransform {

    transform(value: number[]): string {
        if (value.length != 2) {
            return '-';
        }

        let result: string = value[0] + ':';
        if (value[1] < 10) {
            result += '0' + value[1];
        } else {
            result += value[1];
        }
        return result;
    }

}