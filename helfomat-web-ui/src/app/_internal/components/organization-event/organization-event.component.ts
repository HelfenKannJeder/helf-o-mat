import {Component, Input} from "@angular/core";
import {Organization, OrganizationEvent} from "../../resources/organization.service";
import {GeoPoint} from "../../../../_internal/geopoint";

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

    public isDeleteEvent(eventType: string): boolean {
        return eventType.indexOf("EditDelete") >= 0;
    }

    public isCreateEvent(eventType: string): boolean {
        return eventType.indexOf("EditAdd") >= 0;
    }

    public isChange(eventType: string): boolean {
        return !this.isDeleteEvent(eventType) && !this.isCreateEvent(eventType);
    }

    isGeoPointEqual(location: GeoPoint, location2: GeoPoint) {
        return GeoPoint.isEqual(location, location2);
    }
}