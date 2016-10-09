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

        Observable.combineLatest(
            this.position,
            this.distance
        ).subscribe((newSearchRange: [GeoPoint, number]) => {
            var mapsPosition = this.convertGeoPointToLatLng(newSearchRange[0]);
            this.map.setCenter(mapsPosition);
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
            this.updatePosition.next(new GeoPoint(event.latLng.lat(), event.latLng.lng()))
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

}
