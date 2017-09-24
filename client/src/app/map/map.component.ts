import {AfterViewInit, Component, ElementRef, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Organisation} from '../organisation/organisation.model';
import {Observable} from 'rxjs';
import {GeoPoint} from '../organisation/geopoint.model';
import {BoundingBox} from '../organisation/boundingbox.model';
import MarkerClusterer from 'node-js-marker-clusterer';
import {animate, state, style, transition, trigger} from '@angular/animations';
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
    selector: 'helfomat-map',
    templateUrl: './map.component.html',
    styleUrls: ['./map.component.scss'],
    animations: [
        trigger('resizeMap', [
            state('normal', style({
                height: '300px'
            })),
            state('fullscreen', style({
                height: 'calc(100vh - 160px)'
            })),
            transition('fullscreen => normal', animate(MapComponent.MAP_RESIZE_DURATION + 'ms ease-in-out')),
            transition('normal => fullscreen', animate(MapComponent.MAP_RESIZE_DURATION + 'ms ease-in-out'))
        ])
    ]
})
export class MapComponent implements OnInit, AfterViewInit {

    public static readonly MAP_RESIZE_DURATION = 400;

    @Input() organisations?: Observable<Organisation[]>;
    @Input() center: Observable<GeoPoint>;
    @Input() position: Observable<GeoPoint>;
    @Input() distance: Observable<number>;
    @Input() zoom: Observable<number>;
    @Input() clusteredOrganisations: Observable<GeoPoint[]>;
    @Input() allowUpdatePosition: boolean = true;

    @Output() updatePosition: EventEmitter<GeoPoint> = new EventEmitter<GeoPoint>();
    @Output() updateBoundingBox: EventEmitter<BoundingBox> = new EventEmitter<BoundingBox>();
    @Output() updateZoom: EventEmitter<number> = new EventEmitter<number>();
    @Output() openOrganisation: EventEmitter<Organisation> = new EventEmitter<Organisation>();

    private map: Map;
    private markers: Marker[] = [];
    private clusteredMarkers: Marker[] = [];
    private positionMarker: Marker;
    private positionCircle: Circle;
    private markerClusterer: MarkerClusterer;

    public mapSize = 'normal';

    constructor(private element: ElementRef) {
    }

    public toggleMapSize(): void {
        let center = this.map.getCenter();
        this.mapSize = this.mapSize === 'normal' ? 'fullscreen' : 'normal';
        setTimeout(() => {
            google.maps.event.trigger(this.map, 'resize');
            this.map.setCenter(center);
        }, MapComponent.MAP_RESIZE_DURATION);
    }

    ngOnInit() {

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
        if (this.hasClustedOrganisations()) {
            this.drawClusteredOrganisations();
        }

        google.maps.event.trigger(this.map, 'resize');
    }

    private configureViewPortChange() {
        this.map.addListener('bounds_changed', () => {
            let bounds = this.map.getBounds();
            if (bounds == null) {
                return;
            }

            const northEast = MapComponent.convertLatLngToGeoPoint(bounds.getNorthEast());
            const southWest = MapComponent.convertLatLngToGeoPoint(bounds.getSouthWest());

            this.updateBoundingBox.emit(new BoundingBox(northEast, southWest));
        });

        this.map.addListener('zoom_changed', () => {
            this.updateZoom.emit(this.map.getZoom());
        });
    }

    private configureDrawUserPosition() {
        Observable.combineLatest(
            this.position,
            this.distance
        ).subscribe((newSearchRange: [GeoPoint, number]) => {
            const mapsPosition = MapComponent.convertGeoPointToLatLng(newSearchRange[0]);
            this.drawUserPosition(mapsPosition, newSearchRange[1]);
        });
    }

    private configureSearchBox() {
        let searchText: HTMLInputElement = this.element.nativeElement.getElementsByClassName('addressInput')[0];
        let searchBox = new SearchBox(searchText);
        this.map.controls[ControlPosition.TOP_LEFT].push(searchText);

        searchBox.addListener('places_changed', () => {
            let places = searchBox.getPlaces();

            if (places.length != 0) {
                this.updatePosition.next(MapComponent.convertLatLngToGeoPoint(places[0].geometry.location));
            }
        });

        this.map.addListener('bounds_changed', () => {
            let bounds = this.map.getBounds();

            if (bounds != null) {
                searchBox.setBounds(bounds);
            }
        });
    }

    private configureUpdateViewPort() {
        this.center.subscribe((center: GeoPoint) => {
            this.map.setCenter(MapComponent.convertGeoPointToLatLng(center));
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

                        let marker = new Marker({
                            position: MapComponent.convertGeoPointToLatLng(address.location),
                            map: this.map,
                            title: organisation.name,
                            icon: icon,
                            opacity: organisation.scoreNorm / 100
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

    private hasClustedOrganisations() {
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
                    position: MapComponent.convertGeoPointToLatLng(geoPoint),
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
            google.maps.event.addListener(this.positionMarker, 'drag', (event) => {
                this.updatePosition.next(MapComponent.convertLatLngToGeoPoint(event.latLng));
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
