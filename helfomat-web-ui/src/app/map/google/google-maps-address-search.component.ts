import {Component, EventEmitter, Input, NgZone, Output, ViewChild} from "@angular/core";
import {environment} from "../../../environments/environment";
import {Address} from "../../_internal/resources/organization.service";
import Autocomplete = google.maps.places.Autocomplete;

@Component({
    selector: 'google-maps-address-search',
    templateUrl: './google-maps-address-search.component.html'
})
export class GoogleMapsAddressSearchComponent {

    @ViewChild('addressSearchElement')
    public addressSearchElement: any;

    @Input()
    current: Address;

    public addressString: string;

    @Output()
    changeAddress: EventEmitter<Address> = new EventEmitter<Address>();

    constructor(private ngZone: NgZone) {
    }

    ngOnInit() {
        if (this.current !== null && this.current !== undefined) {
            this.addressString = `${this.current.street}, ${this.current.zipcode} ${this.current.city}`;
        }
    }

    ngAfterViewInit() {
        this.configureSearchBox(this.addressSearchElement.nativeElement);
    }

    private configureSearchBox(element: HTMLInputElement) {
        let autocomplete = new Autocomplete(element, {types: ['geocode']});
        autocomplete.setComponentRestrictions({'country': environment.defaults.countries});

        autocomplete.addListener('place_changed', () => {
            this.ngZone.run(() => {
                let streetNumber = '';
                let route = '';
                let locality = '';
                let postalCode = '';
                let place = autocomplete.getPlace();
                const addressComponents = place.address_components;
                for (const addressComponent of addressComponents) {
                    if (addressComponent.types.indexOf("street_number") >= 0) {
                        streetNumber = addressComponent.long_name;
                    } else if (addressComponent.types.indexOf("route") >= 0) {
                        route = addressComponent.long_name;
                    } else if (addressComponent.types.indexOf("locality") >= 0) {
                        locality = addressComponent.long_name;
                    } else if (addressComponent.types.indexOf("postal_code") >= 0) {
                        postalCode = addressComponent.long_name;
                    }
                }
                this.changeAddress.emit({
                    street: (route + ' ' + streetNumber).trim(),
                    zipcode: postalCode,
                    city: locality,
                    location: {
                        lat: place.geometry.location.lat(),
                        lon: place.geometry.location.lng()
                    }
                })

            });
        });
    }


}