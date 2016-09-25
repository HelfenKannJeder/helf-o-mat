import {Component, OnInit, Input} from "@angular/core";
import Organisation from "../organisation/organisation.model";
import Address from "../organisation/address.model";
import {Observable} from "rxjs";
import Map = google.maps.Map;
import Marker = google.maps.Marker;

@Component({
    selector: 'app-map',
    templateUrl: './map.component.html',
    styleUrls: ['./map.component.scss']
})
export class MapComponent implements OnInit {

    @Input() organisations: Observable<Organisation[]>;

    private markers: Marker[] = [];

    constructor() {
    }

    ngOnInit() {
        var mapProp = {
            center: new google.maps.LatLng(49.038883, 8.348804),
            zoom: 11,
            mapTypeId: google.maps.MapTypeId.ROADMAP,
            zoomControl: true,
            mapTypeControl: true,
            scaleControl: true,
            streetViewControl: false,
            rotateControl: false,
            fullscreenControl: true
        };
        var map = new Map(document.getElementById("googleMap"), mapProp);

        this.organisations.subscribe((organisations) => {
            this.markers.forEach((marker) => {
                marker.setMap(null);
            });
            this.markers = [];

            organisations.forEach((organisation: Organisation) => {
                if (organisation.addresses.length > 0) {
                    let address: Address = organisation.addresses[0];
                    this.markers.push(new Marker({
                        position: {lat: address.location.lat, lng: address.location.lon},
                        map: map,
                        title: organisation.name
                    }));
                }
            });
        });
    }

}
