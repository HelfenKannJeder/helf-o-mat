import {
    AfterViewChecked,
    AfterViewInit,
    Component,
    ElementRef,
    EventEmitter,
    Input,
    NgZone,
    OnInit,
    Output,
    ViewChild
} from '@angular/core';
import {combineLatest, Observable} from 'rxjs';
import MarkerClusterer from 'node-js-marker-clusterer';
import {animate, state, style, transition, trigger} from '@angular/animations';
import {Address, BoundingBox, Organization} from '../../_internal/resources/organization.service';
import {GeoPoint} from '../../../_internal/geopoint';
import {environment} from "../../../environments/environment";
import Map = google.maps.Map;
import Marker = google.maps.Marker;
import Circle = google.maps.Circle;
import LatLng = google.maps.LatLng;
import MapTypeId = google.maps.MapTypeId;
import Point = google.maps.Point;
import Size = google.maps.Size;
import ControlPosition = google.maps.ControlPosition;
import Autocomplete = google.maps.places.Autocomplete;

@Component({
    selector: 'helfomat-google-maps',
    templateUrl: './google-maps.component.html',
    styleUrls: ['./google-maps.component.scss'],
    animations: [
        trigger('resizeMap', [
            state('icon', style({
                height: '150px'
            })),
            state('normal', style({
                height: '300px'
            })),
            state('large', style({
                height: '600px'
            })),
            state('popup', style({
                height: '450px'
            })),
            state('fullscreen', style({
                height: 'calc(100vh - 160px)'
            })),
            transition('fullscreen => normal', animate(GoogleMapsComponent.MAP_RESIZE_DURATION + 'ms ease-in-out')),
            transition('normal => fullscreen', animate(GoogleMapsComponent.MAP_RESIZE_DURATION + 'ms ease-in-out'))
        ])
    ]
})
export class GoogleMapsComponent implements OnInit, AfterViewInit, AfterViewChecked {

    public static readonly MAP_RESIZE_DURATION = 400;

    @Input() organizations?: Observable<Organization[]>;
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

    @ViewChild('googleMaps') public googleMapsElement: any;

    private map: Map;
    private markers: Marker[] = [];
    private clusteredMarkers: Marker[] = [];
    private positionMarker: Marker;
    private positionCircle: Circle;
    private markerClusterer: MarkerClusterer;
    private controlButton: HTMLElement;

    private searchContainers: HTMLElement[] = [];

    constructor(
        private element: ElementRef,
        private ngZone: NgZone
    ) {
    }

    ngOnInit() {

    }

    ngOnDestroy() {
        if (this.map != null) {
            this.map.unbindAll();
        }
    }

    ngAfterViewInit() {
        this.map = new Map(this.googleMapsElement.nativeElement, {
            mapTypeId: MapTypeId.ROADMAP,
            zoomControl: true,
            mapTypeControl: false,
            scaleControl: true,
            streetViewControl: false,
            rotateControl: false,
            fullscreenControl: false
        });

        this.configureViewPortChange();
        this.configureDrawUserPosition();
        this.configureUpdateViewPort();

        if (this.canChoosePosition()) {
            this.configureSearchBox();
        }
        if (this.hasOrganizations()) {
            this.configureDrawOrganizations();
        }

        this.markerClusterer = new MarkerClusterer(this.map, [], {imagePath: 'assets/images/m'});
        if (this.hasClusteredOrganizations()) {
            this.drawClusteredOrganizations();
        }

        if (this.showMapResizeButton) {
            this.configureMapResizeButton();
        }
        google.maps.event.trigger(this.map, 'resize');
    }

    ngAfterViewChecked(): void {
        if (this.canChoosePosition()) {
            this.configureSearchBox();
        }
        this.updateMapResizeButton();
    }

    private configureViewPortChange() {
        this.map.addListener('bounds_changed', () => {
            this.ngZone.run(() => {
                let bounds = this.map.getBounds();
                if (bounds == null) {
                    return;
                }

                const northEast = GoogleMapsComponent.convertLatLngToGeoPoint(bounds.getNorthEast());
                const southWest = GoogleMapsComponent.convertLatLngToGeoPoint(bounds.getSouthWest());

                this.updateBoundingBox.emit(new BoundingBox(northEast, southWest));
            });
        });

        if (this.updateZoom != null) {
            this.map.addListener('zoom_changed', () => {
                this.ngZone.run(() => {
                    this.updateZoom.emit(this.map.getZoom());
                });
            });
        }
    }

    private configureDrawUserPosition() {
        if (this.position == null || this.distance == null) {
            return;
        }

        combineLatest([
            this.position,
            this.distance
        ])
            .subscribe(([position, distance]: [GeoPoint, number]) => {
                if (position == null) {
                    return;
                }
                const mapsPosition = GoogleMapsComponent.convertGeoPointToLatLng(position);
                this.drawUserPosition(mapsPosition, distance);
            });
    }

    private configureSearchBox() {
        let addressSearchContainers: HTMLCollectionOf<HTMLElement> = this.element.nativeElement.getElementsByClassName('addressSearchContainer');
        let addressSearchContainersArray: HTMLElement[] = [];
        for (let i = 0; i < addressSearchContainers.length; i++) {
            addressSearchContainersArray.push(addressSearchContainers.item(i));
        }

        for (const addressSearchContainer of this.searchContainers) {
            let index = this.map.controls[ControlPosition.TOP_LEFT].getArray().indexOf(addressSearchContainer);
            if (index >= 0 && addressSearchContainersArray.indexOf(addressSearchContainer) < 0) {
                this.map.controls[ControlPosition.TOP_LEFT].removeAt(index);
            }
        }


        for (const addressSearchContainer of addressSearchContainersArray) {
            if (this.searchContainers.indexOf(addressSearchContainer) >= 0) {
                continue;
            }
            this.searchContainers.push(addressSearchContainer);

            this.map.controls[ControlPosition.TOP_LEFT].push(addressSearchContainer);

            let isInputField = addressSearchContainer.classList.contains('addressInput');
            let searchText: HTMLInputElement;
            if (isInputField) {
                searchText = <HTMLInputElement>addressSearchContainer;
            } else {
                searchText = <HTMLInputElement>addressSearchContainer.getElementsByClassName('addressInput')[0];
            }

            let autocomplete = new Autocomplete(searchText, {types: ['geocode']});
            autocomplete.setComponentRestrictions({'country': environment.defaults.countries});

            autocomplete.addListener('place_changed', () => {
                this.ngZone.run(() => {
                    let place = autocomplete.getPlace();
                    this.updatePosition.next(GoogleMapsComponent.convertLatLngToGeoPoint(place.geometry.location));
                });
            });

            this.map.addListener('bounds_changed', () => {
                let bounds = this.map.getBounds();

                if (bounds != null) {
                    autocomplete.setBounds(bounds);
                }
            });
        }
    }

    private configureUpdateViewPort() {
        this.center.subscribe((center: GeoPoint) => {
            google.maps.event.trigger(this.map, 'resize');
            this.map.setCenter(GoogleMapsComponent.convertGeoPointToLatLng(center));
        });

        this.zoom.subscribe((zoom: number) => {
            this.map.setZoom(zoom);
        });
    }

    private hasOrganizations() {
        return this.organizations !== undefined;
    }

    private configureDrawOrganizations() {
        this.organizations.subscribe((organizations) => {
            this.markers.forEach((marker: Marker) => {
                marker.setMap(null);
            });
            this.markers = [];

            organizations.forEach((organization: Organization) => {
                if (organization.addresses.length > 0 && organization.organizationType !== undefined) {
                    let organizationType = organization.organizationType;
                    let mapPin = GoogleMapsComponent.getMapPin(organizationType);
                    for (let address of organization.addresses) {
                        const icon = {
                            url: `assets/images/pins/${mapPin}`,
                            size: new Size(32, 32),
                            origin: new Point(0, 0),
                            anchor: new Point(10, 32),
                            scaledSize: new Size(32, 32)
                        };

                        let opacity = 1;
                        if (organization.scoreNorm !== null && organization.scoreNorm !== undefined) {
                            opacity = organization.scoreNorm / 100;
                        }
                        if (!Address.isEqual(organization.defaultAddress, address)) {
                            opacity = opacity * 0.5;
                        }
                        let marker = new Marker({
                            position: GoogleMapsComponent.convertGeoPointToLatLng(address.location),
                            map: this.map,
                            title: address.addressAppendix || organization.name,
                            icon,
                            opacity
                        });
                        marker.addListener('click', () => {
                            this.ngZone.run(() => {
                                this.openOrganization.emit(organization);
                            });
                        });
                        this.markers.push(marker);
                    }
                }
            });
        });
    }

    private static getMapPin(organizationType: string) {
        switch (organizationType) {
            case "THW":
                return "blue-pushpin.png";

            case "FF":
                return "red-pushpin.png";

            case "DLRG":
                return "ltblu-pushpin.png";

            case "BW":
                return "grn-pushpin.png";

            case "ASB":
            case "BRK":
            case "DRK":
            case "JUH":
            case "MHD":
            case "KIT":
            case "PRIV_SAN":
            case "BRH":
            case "DRV":
                return "ylw-pushpin.png";
            default:
                return "gray.png";
        }
    }

    private hasClusteredOrganizations() {
        return this.clusteredOrganizations !== undefined;
    }

    private drawClusteredOrganizations() {
        this.clusteredOrganizations.subscribe((clusteredOrganizations) => {
            const icon = {
                url: 'assets/images/pins/gray.png',
                size: new Size(32, 32),
                origin: new Point(0, 0),
                anchor: new Point(10, 32),
                scaledSize: new Size(32, 32)
            };

            let newMarkers = clusteredOrganizations.map((geoPoint: GeoPoint) => {
                return new Marker({
                    position: GoogleMapsComponent.convertGeoPointToLatLng(geoPoint),
                    icon
                });

            });

            this.clusteredMarkers = this.replaceMarkers(this.clusteredMarkers, newMarkers);

        });
    }

    drawUserPosition(position: LatLng, distance: number) {
        if (this.positionMarker !== undefined) {
            this.positionMarker.setMap(null);
            this.positionCircle.setMap(null);
        }

        // Create marker
        this.positionMarker = new Marker({
            map: this.map,
            position: position,
            draggable: this.canChoosePosition()
        });

        if (this.canChoosePosition()) {
            google.maps.event.addListener(this.positionMarker, 'drag', (event: { latLng: LatLng }) => {
                this.updatePosition.next(GoogleMapsComponent.convertLatLngToGeoPoint(event.latLng));
            });
        }

        // Add circle overlay and bind to marker
        this.positionCircle = new Circle({
            map: this.map,
            radius: (distance + 1) * 1000,
            fillOpacity: 0.05,
            strokeWeight: 0.1,
            fillColor: '#000000'
        });
        this.positionCircle.bindTo('center', this.positionMarker, 'position');
    }

    canChoosePosition(): boolean {
        return this.allowUpdatePosition;
    }

    private configureMapResizeButton() {
        this.controlButton = document.createElement('span');
        this.controlButton.className = 'material-icons';
        if (this.mapSize === 'fullscreen') {
            this.controlButton.innerText = "keyboard_arrow_up"
        } else {
            this.controlButton.innerText = 'keyboard_arrow_down';
        }
        this.controlButton.style.fontSize = '48px';
        this.controlButton.style.cursor = 'pointer';
        this.controlButton.addEventListener('click', () => {
            this.ngZone.run(() => {
                let center = this.map.getCenter();
                if (this.mapSize === 'normal') {
                    this.mapSize = 'fullscreen';
                } else {
                    this.mapSize = 'normal';
                }
                this.mapResize.emit(this.mapSize);
                setTimeout(() => {
                    google.maps.event.trigger(this.map, 'resize');
                    this.map.setCenter(center);
                }, GoogleMapsComponent.MAP_RESIZE_DURATION);
            });
        });

        let centerControlDiv: any = document.createElement('div');
        centerControlDiv.appendChild(this.controlButton);
        centerControlDiv.index = 1;
        this.map.controls[google.maps.ControlPosition.BOTTOM_CENTER].push(centerControlDiv);
    }

    private updateMapResizeButton(): void {
        if (this.controlButton === undefined) {
            return;
        }
        if (this.mapSize === 'normal') {
            this.controlButton.innerText = 'keyboard_arrow_down';
        } else {
            this.controlButton.innerText = "keyboard_arrow_up"
        }
    }

    private static convertGeoPointToLatLng(location: GeoPoint): LatLng {
        return new LatLng(location.lat, location.lon);
    }

    private static convertLatLngToGeoPoint(location: LatLng): GeoPoint {
        return new GeoPoint(location.lat(), location.lng());
    }

    private replaceMarkers(oldMarkers: Marker[], newMarkers: Marker[]): Marker[] {
        this.markerClusterer.removeMarkers(oldMarkers, false);
        this.markerClusterer.addMarkers(newMarkers, false);
        this.markerClusterer.repaint();
        return newMarkers;
    }
}
