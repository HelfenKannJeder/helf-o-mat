import {Component, Input} from "@angular/core";
import {Organization, OrganizationEvent} from "../../resources/organization.service";

@Component({
    selector: 'organization-event',
    templateUrl: './organization-event.component.html',
    styleUrls: [
        './organization-event.component.scss'
    ]
})
export class OrganizationEventComponent {

    @Input()
    public organization: Organization;

    @Input()
    public organizationEvents: Array<OrganizationEvent> = [];

}