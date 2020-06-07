import {Component, Input} from "@angular/core";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {PictureId} from "../../../_internal/resources/picture.service";

@Component({
    templateUrl: './edit-logo.component.html',
    styleUrls: ['./edit-logo.component.scss']
})
export class EditLogoComponent {

    @Input()
    public logo: PictureId

    @Input()
    public logoSuggestions: PictureId[];

    constructor(
        public modal: NgbActiveModal
    ) {
    }

}