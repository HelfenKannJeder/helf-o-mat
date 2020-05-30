import {Component, EventEmitter, Input, Output} from "@angular/core";
import {combineLatest, concat, EMPTY, Observable, of} from "rxjs";
import {BoundingBox, Organization} from "../_internal/resources/organization.service";
import {GeoPoint} from "../../_internal/geopoint";
import {environment} from "../../environments/environment";
import {filter, map} from "rxjs/operators";

@Component({
    selector: 'helfomat-single-map',
    templateUrl: './single-map.component.html'
})
export class SingleMapComponent {

    @Input() organization: Observable<Organization>;
    @Input() position?: Observable<GeoPoint>;
    @Input() distance?: Observable<number>;
    @Input() allowUpdatePosition: boolean = true;
    @Input() mapSize = 'normal';
    @Input() showMapResizeButton: boolean = true;
    @Input() showDefaultAddressBar: boolean = true;

    @Output() updatePosition: EventEmitter<GeoPoint> = new EventEmitter<GeoPoint>();
    @Output() updateBoundingBox: EventEmitter<BoundingBox> = new EventEmitter<BoundingBox>();
    @Output() updateZoom: EventEmitter<number> = new EventEmitter<number>();
    @Output() openOrganization: EventEmitter<Organization> = new EventEmitter<Organization>();
    @Output() mapResize: EventEmitter<string> = new EventEmitter<string>();

    public offlineMode: boolean = environment.offline;

    organizations: Observable<Organization[]>;
    center: Observable<GeoPoint>;
    zoom?: Observable<number>;

    ngOnInit(): void {
        if (this.position == null) {
            this.position = EMPTY
        }

        this.organizations = this.organization.pipe(
            map(organization => [organization])
        );

        this.zoom = combineLatest([
            this.organization,
            SingleMapComponent.prefixWithNull(this.position.pipe(filter(position => position != null)))
        ])
            .pipe(
                map(([organization, position]: [Organization, GeoPoint]) => {
                    let location = SingleMapComponent.getOrganizationLocation(organization);
                    return SingleMapComponent.calculateZoomLevel(location, position);
                })
            );

        this.center = combineLatest([
            this.organization,
            SingleMapComponent.prefixWithNull(this.position.pipe(filter(position => position != null)))
        ])
            .pipe(
                map(([organizations, position]: [Organization, GeoPoint]) => {
                    let location = SingleMapComponent.getOrganizationLocation(organizations);
                    return GeoPoint.pointBetween(position, location);
                })
            );


    }

    private static calculateZoomLevel(position1: GeoPoint, position2: GeoPoint): number {
        if (position1 == null || position2 == null) {
            return 12;
        }
        let distanceInKm = GeoPoint.distanceInKm(position1, position2);
        let mapHeight = 200;
        let distanceInM = distanceInKm * 1000;
        let metersPerPixel = (distanceInM / mapHeight);
        let zoom = Math.floor(
            Math.log((156543.03392 * Math.cos(position2.lat * Math.PI / 180)) / metersPerPixel) / Math.log(2)
        );

        if (zoom <= 2) {
            zoom = 2;
        }
        if (zoom >= 20) {
            zoom = 20;
        }
        return zoom - 1;
    }

    private static getOrganizationLocation(organization: Organization): GeoPoint {
        return organization?.defaultAddress?.location;
    }

    private static prefixWithNull<T>(observable: Observable<T>): Observable<T> {
        return concat(of(null), observable);
    }
}