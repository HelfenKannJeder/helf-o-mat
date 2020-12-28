import {Component, Input} from "@angular/core";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {ContactPerson, Organization} from "../../_internal/resources/organization.service";
import {ContactRequestResult} from "../../_internal/resources/contact.service";

@Component({
    templateUrl: './contact-form-confirm.component.html'
})
export class ContactFormConfirmComponent {

    @Input()
    public contact: ContactPerson;

    @Input()
    public organization: Organization;

    @Input()
    public contactRequestResult: ContactRequestResult;

    constructor(
        public modal: NgbActiveModal
    ) {
    }

}