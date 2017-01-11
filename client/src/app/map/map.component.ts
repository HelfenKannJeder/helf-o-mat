import {Component, OnInit, Input, EventEmitter} from "@angular/core";
import Organisation from "../organisation/organisation.model";
import Address from "../organisation/address.model";
import {Observable} from "rxjs";
import Map = google.maps.Map;
import Marker = google.maps.Marker;
import Circle = google.maps.Circle;
import LatLng = google.maps.LatLng;
import MapTypeId = google.maps.MapTypeId;
import Point = google.maps.Point;
import Size = google.maps.Size;
import ControlPosition = google.maps.ControlPosition;
import SearchBox = google.maps.places.SearchBox;
import GeoPoint from "../organisation/geopoint.model";
import {Output} from "@angular/core/src/metadata/directives";
import BoundingBox from "../organisation/boundingbox.model";
import ClusteredGeoPoint from "../organisation/clusteredGeoPoint.model";

@Component({
    selector: 'helfomat-map',
    templateUrl: './map.component.html',
    styleUrls: ['./map.component.scss']
})
export class MapComponent implements OnInit {

    @Input() organisations: Observable<Organisation[]>;
    @Input() position: Observable<GeoPoint>;
    @Input() distance: Observable<number>;
    @Input() clusteredOrganisations: Observable<ClusteredGeoPoint[]>;

    @Output() updatePosition: EventEmitter<GeoPoint> = new EventEmitter<GeoPoint>();
    @Output() updateBoundingBox: EventEmitter<BoundingBox> = new EventEmitter<BoundingBox>();
    @Output() updateZoom: EventEmitter<number> = new EventEmitter<number>();

    private map: Map;
    private markers: Marker[] = [];
    private clusteredMarkers: Marker[] = [];
    private positionMarker: Marker;
    private positionCircle: Circle;

    constructor() {
    }

    ngOnInit() {
        let mapProp = {
            center: new LatLng(51.163375, 10.447683),
            zoom: 12,
            mapTypeId: MapTypeId.ROADMAP,
            zoomControl: true,
            mapTypeControl: false,
            scaleControl: true,
            streetViewControl: false,
            rotateControl: false,
            fullscreenControl: true
        };
        this.map = new Map(document.getElementById("googleMap"), mapProp);

        // Create the search box and link it to the UI element.
        let searchText : HTMLInputElement = <HTMLInputElement>document.getElementById('pac-input');
        let searchBox = new SearchBox(searchText);
        this.map.controls[ControlPosition.TOP_LEFT].push(searchText);

        searchBox.addListener('places_changed', () => {
            let places = searchBox.getPlaces();

            if (places.length == 0) {
                return;
            }

            this.updatePosition.next(MapComponent.convertLatLngToGeoPoint(places[0].geometry.location));
        });

        this.map.addListener('bounds_changed', () => {
            searchBox.setBounds(this.map.getBounds());

            const northEast = MapComponent.convertLatLngToGeoPoint(this.map.getBounds().getNorthEast());
            const southWest = MapComponent.convertLatLngToGeoPoint(this.map.getBounds().getSouthWest());

            this.updateBoundingBox.emit(new BoundingBox(northEast, southWest));
        });

        this.map.addListener('zoom_changed', () => {
            this.updateZoom.emit(this.map.getZoom());
        });

        Observable.combineLatest(
            this.position,
            this.distance
        ).subscribe((newSearchRange: [GeoPoint, number]) => {
            const mapsPosition = MapComponent.convertGeoPointToLatLng(newSearchRange[0]);
            if (this.map.getBounds() == undefined
                || !this.map.getBounds().contains(mapsPosition)) {
                this.map.setCenter(mapsPosition);
            }
            this.drawUserPosition(mapsPosition, newSearchRange[1]);
        });

        this.organisations.subscribe((organisations) => {
            this.markers.forEach((marker: Marker) => {
                marker.setMap(null);
            });
            this.markers = [];

            organisations.forEach((organisation: Organisation) => {
                if (organisation.addresses.length > 0 && organisation.mapPin !== undefined) {
                    const address: Address = organisation.addresses[0];
                    const icon = {
                        url: "https://helfenkannjeder.de/uploads/pics/" + organisation.mapPin,
                        size: new Size(32, 32),
                        origin: new Point(0, 0),
                        anchor: new Point(10, 32),
                        scaledSize: new Size(32, 32)
                    };

                    this.markers.push(new Marker({
                        position: MapComponent.convertGeoPointToLatLng(address.location),
                        map: this.map,
                        title: organisation.name,
                        icon: icon,
                        opacity: organisation.scoreNorm / 100
                    }));
                }
            });
        });

        this.clusteredOrganisations.subscribe((clusteredOrganisations) => {
            const icon = {
                size: new Size(64, 64),
                origin: new Point(0, 0),
                anchor: new Point(32, 32),
                scaledSize: new Size(64, 64)
            };

            let newMarkers = clusteredOrganisations.map((clusteredOrganisation: ClusteredGeoPoint) => {
                let index = MapComponent.getClusteredIndexIconIndex(clusteredOrganisation.count, 5);

                return new Marker({
                    position: MapComponent.convertGeoPointToLatLng(clusteredOrganisation.geoPoint),
                    map: null,
                    label: clusteredOrganisation.count.toLocaleString(),
                    icon: {
                        url: 'assets/images/m' + index + '.png',
                        size: icon.size,
                        origin: icon.origin,
                        anchor: icon.anchor,
                        scaledSize: icon.scaledSize
                    }
                });

            });

            this.clusteredMarkers = this.replaceMarkers(this.clusteredMarkers, newMarkers);

        });
    }

    private static getClusteredIndexIconIndex(count: number, numStyles: number) {
        let index = 0;
        let dv: number = count;
        while (dv >= 1) {
            dv = dv / 10;
            index++;
        }
        return Math.min(index, numStyles);
    }

    ngAfterViewInit() {
        google.maps.event.trigger(this.map, "resize");
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
            draggable: true
        });

        google.maps.event.addListener(this.positionMarker, "drag", (event) => {
            this.updatePosition.next(MapComponent.convertLatLngToGeoPoint(event.latLng));
        });

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

    private static convertGeoPointToLatLng(location: GeoPoint): LatLng {
        return new LatLng(location.lat, location.lon);
    }

    private static convertLatLngToGeoPoint(location: LatLng): GeoPoint {
        return new GeoPoint(location.lat(), location.lng());
    }

    private replaceMarkers(oldMarkers: Marker[], newMarkers: Marker[]): Marker[] {
        oldMarkers.forEach((marker: Marker) => {
            marker.setMap(null);
        });
        newMarkers.forEach((marker: Marker) => {
            marker.setMap(this.map);
        });
        return newMarkers;
    }
}
