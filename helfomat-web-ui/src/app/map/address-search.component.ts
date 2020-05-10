import {Component, EventEmitter, Input, Output} from "@angular/core";
import {Address} from "../_internal/resources/organization.service";

@Component({
    selector: 'address-search',
    templateUrl: './address-search.component.html'
})
export class AddressSearchComponent {

    @Input()
    current: Address;

    @Output()
    changeAddress: EventEmitter<Address> = new EventEmitter<Address>();

}