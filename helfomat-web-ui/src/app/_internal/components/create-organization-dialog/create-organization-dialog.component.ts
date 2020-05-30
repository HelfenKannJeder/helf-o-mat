import {Component, Input} from "@angular/core";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {OrganizationType} from "../../resources/organization.service";

@Component({
    selector: 'create-organization-dialog',
    templateUrl: './create-organization-dialog.component.html'
})
export class CreateOrganizationDialogComponent {

    public organizationType: string;

    @Input()
    public organizationTypes: OrganizationType[];

    constructor(
        public modal: NgbActiveModal
    ) {
    }

}