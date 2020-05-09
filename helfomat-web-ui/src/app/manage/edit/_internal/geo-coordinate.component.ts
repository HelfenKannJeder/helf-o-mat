import {Component, EventEmitter, Input, Output} from "@angular/core";

@Component({
    selector: 'geo-coordinate',
    templateUrl: './geo-coordinate.component.html'
})
export class GeoCoordinateComponent {
    degree: number;
    minutes: number;
    seconds: number;

    @Input()
    coordinate: number;

    @Output()
    coordinateChange: EventEmitter<number> = new EventEmitter<number>();

    ngOnInit() {
        if (this.coordinate) {
            this.degree = this.geoToDegree(this.coordinate);
            this.minutes = this.geoToMinutes(this.coordinate);
            this.seconds = this.geoToSeconds(this.coordinate);
        }
    }

    geoToDegree(point: number) {
        return Math.floor(point);
    }

    geoToMinutes(point: number) {
        const afterTheDot = point % 1;
        const minutes = afterTheDot * 60;
        return Math.floor(minutes);
    }

    geoToSeconds(point: number) {
        const afterTheDot = point % 1;
        const minutes = afterTheDot * 60;
        return (minutes % 1) * 60;
    }

    change() {
        const coordinate = this.degree + (this.minutes / 60) + (this.seconds / 3600);
        this.coordinateChange.next(coordinate);
    }
}