import {NgModule} from "@angular/core";
import {QrCodeComponent} from "./qr-code.component";
import {QRCodeModule} from "angularx-qrcode";
import {NgbModalModule} from "@ng-bootstrap/ng-bootstrap";
import {QrCodeModalComponent} from "./qr-code-modal.component";

@NgModule({
    imports: [
        QRCodeModule,
        NgbModalModule
    ],
    declarations: [
        QrCodeComponent,
        QrCodeModalComponent
    ],
    exports: [
        QrCodeComponent
    ]
})
export class QrCodeModule {
}