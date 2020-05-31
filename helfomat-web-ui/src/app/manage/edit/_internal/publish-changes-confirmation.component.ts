import {Component, Input} from "@angular/core";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {Organization, OrganizationEvent} from "../../../_internal/resources/organization.service";
import {NgForm, NgModel} from "@angular/forms";

@Component({
    selector: 'publish-changes-confirmation',
    templateUrl: './publish-changes-confirmation.component.html'
})
export class PublishChangesConfirmationComponent {

    @Input()
    public publish: PublishContent = {} as PublishContent;

    @Input()
    public activeTab: number = 0;

    @Input()
    public isFormValid: boolean = true;

    constructor(
        public modal: NgbActiveModal
    ) {
    }

    openTab(tab: number) {
        this.activeTab = tab;
    }

    public hasError(field: NgModel): boolean {
        return field.invalid && (field.dirty || field.touched);
    }

    public publishOrganization(formElement: NgForm) {
        for (const i in formElement.controls) {
            formElement.controls[i].markAsTouched();
        }
        if (formElement.valid) {
            this.modal.close(this.publish);
        }
    }
}

export interface PublishContent {
    organization: Organization;
    describeSources: string;
    changes: Array<OrganizationEvent>;
}