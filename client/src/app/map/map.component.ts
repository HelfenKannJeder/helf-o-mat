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

@Component({
    selector: 'helfomat-map',
    templateUrl: './map.component.html',
    styleUrls: ['./map.component.scss']
})
export class MapComponent implements OnInit {

    @Input() organisations: Observable<Organisation[]>;
    @Input() position: Observable<GeoPoint>;
    @Input() distance: Observable<number>;

    @Output() updatePosition: EventEmitter<GeoPoint> = new EventEmitter<GeoPoint>();

    private map: Map;
    private markers: Marker[] = [];
    private positionMarker: Marker;
    private positionCircle: Circle;

    constructor() {
    }

    ngOnInit() {
        var mapProp = {
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
        var input = document.getElementById('pac-input');
        var searchBox = new SearchBox(input);
        this.map.controls[ControlPosition.TOP_LEFT].push(input);

        // Bias the SearchBox results towards current map's viewport.
        this.map.addListener('bounds_changed', () => {
            searchBox.setBounds(this.map.getBounds());
        });

        searchBox.addListener('places_changed', () => {
            var places = searchBox.getPlaces();

            if (places.length == 0) {
                return;
            }

            this.updatePosition.next(this.convertLatLngToGeoPoint(places[0].geometry.location));
        });

        google.maps.event.addListener(this.map, "bounds_changed", () => {
            var northEast = this.convertLatLngToGeoPoint(this.map.getBounds().getNorthEast());
            var southWest = this.convertLatLngToGeoPoint(this.map.getBounds().getSouthWest());
            console.log("map bounds{", northEast, southWest);
        });

        Observable.combineLatest(
            this.position,
            this.distance
        ).subscribe((newSearchRange: [GeoPoint, number]) => {
            var mapsPosition = this.convertGeoPointToLatLng(newSearchRange[0]);
            if (this.map.getBounds() == undefined
                || !this.map.getBounds().contains(mapsPosition)) {
                this.map.setCenter(mapsPosition);
            }
            this.drawUserPosition(mapsPosition, newSearchRange[1]);
        });

        this.organisations.subscribe((organisations) => {
            this.markers.forEach((marker) => {
                marker.setMap(null);
            });
            this.markers = [];

            organisations.forEach((organisation: Organisation) => {
                if (organisation.addresses.length > 0 && organisation.mapPin !== undefined) {
                    let address: Address = organisation.addresses[0];
                    var icon = {
                        url: "https://helfenkannjeder.de/uploads/pics/" + organisation.mapPin,
                        size: new Size(32, 32),
                        origin: new Point(0, 0),
                        anchor: new Point(10, 32),
                        scaledSize: new Size(32, 32)
                    };

                    this.markers.push(new Marker({
                        position: this.convertGeoPointToLatLng(address.location),
                        map: this.map,
                        title: organisation.name,
                        icon: icon,
                        opacity: organisation._scoreNorm / 100
                    }));
                }
            });
        });
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
            this.updatePosition.next(this.convertLatLngToGeoPoint(event.latLng));
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

    private convertGeoPointToLatLng(location: GeoPoint): LatLng {
        return new LatLng(location.lat, location.lon);
    }

    private convertLatLngToGeoPoint(location: LatLng): GeoPoint {
        return new GeoPoint(location.lat(), location.lng());
    }

}
