import {Component, Input} from "@angular/core";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {QrCodeModalComponent} from "./qr-code-modal.component";

@Component({
    selector: 'qr-code',
    templateUrl: './qr-code.component.html'
})
export class QrCodeComponent {

    @Input()
    public link: string;

    constructor(
        private modalService: NgbModal
    ) {
    }

    public open() {
        const modalRef = this.modalService.open(QrCodeModalComponent, {
            size: 'lg',
        });
        modalRef.componentInstance.link = this.link;
    }

}