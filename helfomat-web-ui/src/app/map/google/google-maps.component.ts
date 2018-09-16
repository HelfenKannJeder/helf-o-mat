import {
    AfterViewChecked,
    AfterViewInit,
    Component,
    ElementRef,
    EventEmitter,
    Input,
    OnInit,
    Output
} from '@angular/core';
import {Observable} from 'rxjs';
import MarkerClusterer from 'node-js-marker-clusterer';
import {animate, state, style, transition, trigger} from '@angular/animations';
import {BoundingBox, Organisation} from '../../_internal/resources/organisation.service';
import {GeoPoint} from '../../../_internal/geopoint';
import Map = google.maps.Map;
import Marker = google.maps.Marker;
import Circle = google.maps.Circle;
import LatLng = google.maps.LatLng;
import MapTypeId = google.maps.MapTypeId;
import Point = google.maps.Point;
import Size = google.maps.Size;
import ControlPosition = google.maps.ControlPosition;
import SearchBox = google.maps.places.SearchBox;

@Component({
    selector: 'helfomat-google-maps',
    templateUrl: './google-maps.component.html',
    styleUrls: ['./google-maps.component.scss'],
    animations: [
        trigger('resizeMap', [
            state('normal', style({
                height: '300px'
            })),
            state('large', style({
                height: '600px'
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

    private map: Map;
    private markers: Marker[] = [];
    private clusteredMarkers: Marker[] = [];
    private positionMarker: Marker;
    private positionCircle: Circle;
    private markerClusterer: MarkerClusterer;
    private controlButton: HTMLElement;

    private searchContainers: HTMLElement[] = [];

    constructor(private element: ElementRef) {
    }

    ngOnInit() {

    }

    ngOnDestroy() {
        if (this.map != null) {
            this.map.unbindAll();
        }
    }

    ngAfterViewInit() {
        this.map = new Map(this.element.nativeElement.getElementsByClassName('googleMap')[0], {
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
        if (this.hasOrganisations()) {
            this.configureDrawOrganisations();
        }

        this.markerClusterer = new MarkerClusterer(this.map, [], {imagePath: 'assets/images/m'});
        if (this.hasClusteredOrganisations()) {
            this.drawClusteredOrganisations();
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
            let bounds = this.map.getBounds();
            if (bounds == null) {
                return;
            }

            const northEast = GoogleMapsComponent.convertLatLngToGeoPoint(bounds.getNorthEast());
            const southWest = GoogleMapsComponent.convertLatLngToGeoPoint(bounds.getSouthWest());

            this.updateBoundingBox.emit(new BoundingBox(northEast, southWest));
        });

        if (this.updateZoom != null) {
            this.map.addListener('zoom_changed', () => {
                this.updateZoom.emit(this.map.getZoom());
            });
        }
    }

    private configureDrawUserPosition() {
        if (this.position == null || this.distance == null) {
            return;
        }

        Observable.combineLatest(
            this.position,
            this.distance
        ).subscribe(([position, distance]: [GeoPoint, number]) => {
            if (position == null) {
                return;
            }
            const mapsPosition = GoogleMapsComponent.convertGeoPointToLatLng(position);
            this.drawUserPosition(mapsPosition, distance);
        });
    }

    private configureSearchBox() {
        let addressSearchContainers: HTMLElement[] = this.element.nativeElement.getElementsByClassName('addressSearchContainer');
        for (const addressSearchContainer of addressSearchContainers) {
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

            let searchBox = new SearchBox(searchText);

            searchBox.addListener('places_changed', () => {
                let places = searchBox.getPlaces();

                if (places.length != 0) {
                    this.updatePosition.next(GoogleMapsComponent.convertLatLngToGeoPoint(places[0].geometry.location));
                }
            });

            this.map.addListener('bounds_changed', () => {
                let bounds = this.map.getBounds();

                if (bounds != null) {
                    searchBox.setBounds(bounds);
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

    private hasOrganisations() {
        return this.organisations !== undefined;
    }

    private configureDrawOrganisations() {
        this.organisations.subscribe((organisations) => {
            this.markers.forEach((marker: Marker) => {
                marker.setMap(null);
            });
            this.markers = [];

            organisations.forEach((organisation: Organisation) => {
                if (organisation.addresses.length > 0 && organisation.mapPin !== undefined) {
                    for (let address of organisation.addresses) {
                        const icon = {
                            url: `assets/images/pins/${organisation.mapPin}`,
                            size: new Size(32, 32),
                            origin: new Point(0, 0),
                            anchor: new Point(10, 32),
                            scaledSize: new Size(32, 32)
                        };

                        let opacity = 1;
                        if (organisation.scoreNorm !== null) {
                            opacity = organisation.scoreNorm / 100;
                        }
                        let marker = new Marker({
                            position: GoogleMapsComponent.convertGeoPointToLatLng(address.location),
                            map: this.map,
                            title: organisation.name,
                            icon,
                            opacity
                        });
                        marker.addListener('click', () => {
                            this.openOrganisation.emit(organisation);
                        });
                        this.markers.push(marker);
                    }
                }
            });
        });
    }

    private hasClusteredOrganisations() {
        return this.clusteredOrganisations !== undefined;
    }

    private drawClusteredOrganisations() {
        this.clusteredOrganisations.subscribe((clusteredOrganisations) => {
            const icon = {
                url: 'assets/images/pins/gray.png',
                size: new Size(32, 32),
                origin: new Point(0, 0),
                anchor: new Point(10, 32),
                scaledSize: new Size(32, 32)
            };

            let newMarkers = clusteredOrganisations.map((geoPoint: GeoPoint) => {
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
        if (this.mapSize === 'fullscreen') {
            this.controlButton.className = 'glyphicon glyphicon-chevron-up';
        } else {
            this.controlButton.className = 'glyphicon glyphicon-chevron-down';
        }
        this.controlButton.style.fontSize = '24px';
        this.controlButton.style.marginBottom = '22px';
        this.controlButton.style.cursor = 'pointer';
        this.controlButton.addEventListener('click', () => {
            let center = this.map.getCenter();
            if (this.mapSize === 'normal') {
                this.mapSize = 'fullscreen';
            } else {
                this.mapSize = 'normal';
            }
            console.log('map resize emit', this.mapSize);
            this.mapResize.emit(this.mapSize);
            setTimeout(() => {
                google.maps.event.trigger(this.map, 'resize');
                this.map.setCenter(center);
            }, GoogleMapsComponent.MAP_RESIZE_DURATION);
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
            this.controlButton.className = 'glyphicon glyphicon-chevron-down';
        } else {
            this.controlButton.className = 'glyphicon glyphicon-chevron-up';
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
