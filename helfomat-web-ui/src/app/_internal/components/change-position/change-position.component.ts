import {Component, Input} from "@angular/core";

@Component({
    selector: 'change-position',
    templateUrl: './change-position.component.html',
    styleUrls: ['./change-position.component.scss']
})
export class ChangePositionComponent {

    @Input()
    public indexOffset: number;

    public abs(number: number): number {
        return Math.abs(number);
    }


}