import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Observable} from 'rxjs';
import {BoundingBox, Organization} from '../../_internal/resources/organization.service';
import {GeoPoint} from '../../../_internal/geopoint';

@Component({
    selector: 'map',
    templateUrl: './alternative-map.component.html'
})
export class AlternativeMapComponent {

    @Input() organizations: Observable<Organization[]>;
    @Input() center: Observable<GeoPoint>;
    @Input() position: Observable<GeoPoint>;
    @Input() distance: Observable<number>;
    @Input() zoom: Observable<number>;
    @Input() clusteredOrganizations: Observable<GeoPoint[]>;
    @Input() allowUpdatePosition: boolean = true;
    @Input() mapSize = 'normal';
    @Input() showMapResizeButton: boolean = true;
    @Input() showDefaultAddressBar: boolean = true;

    @Output() updatePosition: EventEmitter<GeoPoint> = new EventEmitter<GeoPoint>();
    @Output() updateBoundingBox: EventEmitter<BoundingBox> = new EventEmitter<BoundingBox>();
    @Output() updateZoom: EventEmitter<number> = new EventEmitter<number>();
    @Output() openOrganization: EventEmitter<Organization> = new EventEmitter<Organization>();
    @Output() mapResize: EventEmitter<string> = new EventEmitter<string>();

    public doUpdatePosition(): void {
        this.updatePosition.next(new GeoPoint(49, 8.5));
    }

}