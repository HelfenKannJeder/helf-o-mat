import {Component, EventEmitter, Input, Output} from "@angular/core";
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

    @Input()
    public selectable: boolean = false;

    @Output()
    public enabledOrganizations: EventEmitter<Array<OrganizationEvent>> = new EventEmitter<Array<OrganizationEvent>>();

    private organizationEventMapping: Map<OrganizationEvent, Boolean> = new Map<OrganizationEvent, Boolean>();

    public isDeleteEvent(eventType: string): boolean {
        return eventType.indexOf("EditDelete") >= 0;
    }

    public isCreateEvent(eventType: string): boolean {
        return eventType.indexOf("EditAdd") >= 0;
    }

    public isChange(eventType: string): boolean {
        return !this.isDeleteEvent(eventType) && !this.isCreateEvent(eventType);
    }

    public toggleEvent(organizationEvent: OrganizationEvent, $event) {
        this.organizationEventMapping.set(organizationEvent, $event.target.checked);
        this.enabledOrganizations.emit(this.getEnabledEvents());
    }

    isGeoPointEqual(location: GeoPoint, location2: GeoPoint) {
        return GeoPoint.isEqual(location, location2);
    }

    private getEnabledEvents(): Array<OrganizationEvent> {
        const events = [];
        for (const organizationEvent of this.organizationEvents) {
            if (!this.organizationEventMapping.has(organizationEvent) || this.organizationEventMapping.get(organizationEvent)) {
                events.push(organizationEvent);
            }
        }
        return events;
    }
}