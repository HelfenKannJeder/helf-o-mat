import {Component, Input} from "@angular/core";
import {ContactPerson, Organization} from "../../_internal/resources/organization.service";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
    templateUrl: './contact-form.component.html'
})
export class ContactFormComponent {

    @Input()
    public contact: ContactPerson;

    @Input()
    public organization: Organization;

    public contactFormContent: ContactFormContent;

    constructor(
        public modal: NgbActiveModal
    ) {
    }
}

export interface ContactFormContent {
    name: string;
    subject: string;
    body: string;
}