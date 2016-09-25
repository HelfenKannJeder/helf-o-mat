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

    constructor() {
    }

    ngOnInit() {
        var mapProp = {
            center: new LatLng(51.163375, 10.447683),
            zoom: 12,
            mapTypeId: MapTypeId.ROADMAP,
            zoomControl: true,
            mapTypeControl: true,
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
    }

    private convertGeoPointToLatLng(location: GeoPoint): LatLng {
        return new LatLng(location.lat, location.lon);
    }

}
