import {Component, EventEmitter, Input, Output} from "@angular/core";
import {Address} from "../../_internal/resources/organization.service";

@Component({
    selector: 'google-maps-address-search',
    template: ''
})
export class NoneAddressSearchComponent {

    @Input()
    current: Address;

    @Output()
    changeAddress: EventEmitter<Address> = new EventEmitter<Address>();

}