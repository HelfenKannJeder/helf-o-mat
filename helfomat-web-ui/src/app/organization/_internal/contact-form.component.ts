import {Component, Input} from "@angular/core";
import {ContactPerson, Organization} from "../../_internal/resources/organization.service";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {ContactService} from "../../_internal/resources/contact.service";

@Component({
    templateUrl: './contact-form.component.html'
})
export class ContactFormComponent {

    @Input()
    public contact: ContactPerson;

    @Input()
    public organization: Organization;

    public contactFormContent: ContactFormContent = {
        email: '',
        subject: '',
        message: ''
    };

    constructor(
        public modal: NgbActiveModal,
        private contactService: ContactService
    ) {
    }

    public submit() {
        this.contactService.createContactRequest({
            email: this.contactFormContent.email,
            subject: this.contactFormContent.subject,
            message: this.contactFormContent.message
        })
            .subscribe(() => {
                this.modal.close({});
            });
    }

}

export interface ContactFormContent {
    email: string;
    subject: string;
    message: string;
}