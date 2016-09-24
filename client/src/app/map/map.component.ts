import {Component, OnInit, Input} from "@angular/core";
import Organisation from "../organisation/organisation.model";
import Address from "../organisation/address.model";

@Component({
    selector: 'app-map',
    templateUrl: './map.component.html',
    styleUrls: ['./map.component.scss']
})
export class MapComponent implements OnInit {

    @Input() organisations: Organisation[];

    constructor() {
    }

    ngOnInit() {
        var mapProp = {
            center: new google.maps.LatLng(49.038883, 8.348804),
            zoom: 5,
            mapTypeId: google.maps.MapTypeId.ROADMAP,
            zoomControl: true,
            mapTypeControl: true,
            scaleControl: true,
            streetViewControl: false,
            rotateControl: false,
            fullscreenControl: true
        };
        var map = new google.maps.Map(document.getElementById("googleMap"), mapProp);

        console.log(this.organisations);
        this.organisations.forEach((organisation: Organisation) => {
            let address: Address = organisation.addresses[0];
            console.log(address);
            var marker = new google.maps.Marker({
                position: {lat: address.location.lat, lng: address.location.lon},
                map: map,
                title: organisation.name
            });
        });

    }

}
