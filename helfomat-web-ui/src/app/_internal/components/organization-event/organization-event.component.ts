import {Component, Input} from "@angular/core";
import {Organisation, OrganizationEvent} from "../../resources/organisation.service";

@Component({
    selector: 'organization-event',
    templateUrl: './organization-event.component.html',
    styleUrls: [
        './organization-event.component.scss'
    ]
})
export class OrganizationEventComponent {

    @Input()
    public organization: Organisation;

    @Input()
    public organizationEvents: Array<OrganizationEvent> = [];

}