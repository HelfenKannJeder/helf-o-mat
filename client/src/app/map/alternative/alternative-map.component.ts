import {Component, EventEmitter, Input, Output} from '@angular/core';
import Organisation from '../../organisation/organisation.model';
import BoundingBox from '../../organisation/boundingbox.model';
import GeoPoint from '../../organisation/geopoint.model';
import {Observable} from 'rxjs/Observable';

@Component({
    selector: 'helfomat-map',
    templateUrl: './alternative-map.component.html'
})
export class AlternativeMapComponent {

    @Input() organisations: Observable<Organisation[]>;
    @Input() position: Observable<GeoPoint>;
    @Input() distance: Observable<number>;
    @Input() zoom: Observable<number>;
    @Input() clusteredOrganisations: Observable<GeoPoint[]>;

    @Output() updatePosition: EventEmitter<GeoPoint> = new EventEmitter<GeoPoint>();
    @Output() updateBoundingBox: EventEmitter<BoundingBox> = new EventEmitter<BoundingBox>();
    @Output() updateZoom: EventEmitter<number> = new EventEmitter<number>();
    @Output() openOrganisation: EventEmitter<Organisation> = new EventEmitter<Organisation>();

}