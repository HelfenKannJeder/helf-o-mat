import {Component, OnInit, Input} from "@angular/core";
import Organisation from "../organisation/organisation.model";
import Address from "../organisation/address.model";
import {Observable} from "rxjs";
import Map = google.maps.Map;
import Marker = google.maps.Marker;
import Circle = google.maps.Circle;
import LatLng = google.maps.LatLng;
import MapTypeId = google.maps.MapTypeId;
import GeoPoint from "../organisation/geopoint.model";

@Component({
    selector: 'helfomat-map',
    templateUrl: './map.component.html',
    styleUrls: ['./map.component.scss']
})
export class MapComponent implements OnInit {

    @Input() organisations: Observable<Organisation[]>;
    @Input() position: Observable<GeoPoint>;
    @Input() distance: Observable<number>;

    private map: Map;
    private markers: Marker[] = [];
    private positionMarker: Marker;
    private positionCircle: Circle;
    private currentPosition: LatLng;
    private currentDistance: number;

    constructor() {
    }

    ngOnInit() {
        var mapProp = {
            center: new google.maps.LatLng(49.038883, 8.348804),
            zoom: 11,
            mapTypeId: MapTypeId.ROADMAP,
            zoomControl: true,
            mapTypeControl: true,
            scaleControl: true,
            streetViewControl: false,
            rotateControl: false,
            fullscreenControl: true
        };
        this.map = new Map(document.getElementById("googleMap"), mapProp);

        this.position.subscribe((newPosition: GeoPoint) => {
            var mapsPosition = this.convertGeoPointToLatLng(newPosition);
            this.map.setCenter(mapsPosition);
            this.drawUserPosition(mapsPosition, this.currentDistance);
        });

        this.distance.subscribe((distance: number) => {
            this.drawUserPosition(this.currentPosition, distance);
        });

        this.organisations.subscribe((organisations) => {
            this.markers.forEach((marker) => {
                marker.setMap(null);
            });
            this.markers = [];

            organisations.forEach((organisation: Organisation) => {
                if (organisation.addresses.length > 0) {
                    let address: Address = organisation.addresses[0];
                    this.markers.push(new Marker({
                        position: this.convertGeoPointToLatLng(address.location),
                        map: this.map,
                        title: organisation.name
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
            position: position
        });

        // Add circle overlay and bind to marker
        this.positionCircle = new Circle({
            map: this.map,
            radius: distance * 1000,
            fillOpacity: 0.05,
            strokeWeight: 0.1,
            fillColor: '#000000'
        });
        this.positionCircle.bindTo('center', this.positionMarker, 'position');

        this.currentPosition = position;
        this.currentDistance = distance;
    }

    private convertGeoPointToLatLng(location: GeoPoint): LatLng {
        return new LatLng(location.lat, location.lon);
    }

}
