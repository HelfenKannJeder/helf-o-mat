import {Component, Input} from "@angular/core";
import {ContactPerson} from "../../../_internal/resources/organization.service";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {NgForm, NgModel} from "@angular/forms";

@Component({
    templateUrl: './edit-contact-person.component.html',
    styleUrls: [
        './edit-contact-person.component.scss'
    ]
})
export class EditContactPersonComponent {

    @Input()
    public contactPerson: ContactPerson;

    constructor(
        public modal: NgbActiveModal
    ) {
    }

    public hasError(field: NgModel): boolean {
        return field.invalid && (field.dirty || field.touched);
    }

    public confirmChange(formElement: NgForm) {
        if (formElement.invalid) {
            for (const i in formElement.controls) {
                formElement.controls[i].markAsTouched();
            }
        } else {
            this.modal.close(this.contactPerson);
        }
    }

}