import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Observable} from 'rxjs';
import {environment} from '../../environments/environment';
import {BoundingBox, Organisation} from '../_internal/resources/organisation.service';
import {GeoPoint} from '../../_internal/geopoint';

@Component({
    selector: 'helfomat-map',
    templateUrl: './map.component.html'
})
export class MapComponent {

    @Input() organisations?: Observable<Organisation[]>;
    @Input() center: Observable<GeoPoint>;
    @Input() position: Observable<GeoPoint>;
    @Input() distance: Observable<number>;
    @Input() zoom: Observable<number>;
    @Input() clusteredOrganisations: Observable<GeoPoint[]>;
    @Input() allowUpdatePosition: boolean = true;
    @Input() mapSize = 'normal';
    @Input() showMapResizeButton: boolean = true;
    @Input() showDefaultAddressBar: boolean = true;

    @Output() updatePosition: EventEmitter<GeoPoint> = new EventEmitter<GeoPoint>();
    @Output() updateBoundingBox: EventEmitter<BoundingBox> = new EventEmitter<BoundingBox>();
    @Output() updateZoom: EventEmitter<number> = new EventEmitter<number>();
    @Output() openOrganisation: EventEmitter<Organisation> = new EventEmitter<Organisation>();
    @Output() mapResize: EventEmitter<string> = new EventEmitter<string>();

    public offlineMode: boolean = environment.offline;

}