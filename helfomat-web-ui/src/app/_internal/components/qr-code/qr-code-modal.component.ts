import {Component, Input} from "@angular/core";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
    templateUrl: './qr-code-modal.component.html',
    styleUrls: [
        './qr-code-modal.component.scss'
    ]
})
export class QrCodeModalComponent {

    @Input()
    public link: string;

    constructor(
        public modal: NgbActiveModal
    ) {
    }
}