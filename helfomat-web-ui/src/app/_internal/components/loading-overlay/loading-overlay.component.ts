import {Component, Inject} from "@angular/core";
import {MESSAGE_DATA} from "./loading-overlay.service";

@Component({
    templateUrl: './loading-overlay.component.html',
    styleUrls: [
        './loading-overlay.component.scss'
    ]
})
export class LoadingOverlayComponent {

    constructor(
        @Inject(MESSAGE_DATA) public message: string
    ) {
    }

}