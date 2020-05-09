import {Component, Input} from "@angular/core";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {Address, Organization} from "../../../_internal/resources/organization.service";
import {BehaviorSubject, Observable, Subject} from "rxjs";
import {map} from "rxjs/operators";

@Component({
    templateUrl: './edit-address.component.html'
})
export class EditAddressComponent {

    @Input()
    private address: Address;

    @Input()
    private organization: Organization;

    private _address$: Subject<Address>;
    public address$: Observable<Address>;
    public organization$: Observable<Organization>;

    constructor(
        public modal: NgbActiveModal
    ) {
    }

    onAddressChanged(address: Address) {
        this._address$.next({
            ...address,
            addressAppendix: this.address.addressAppendix,
            telephone: this.address.telephone,
            website: this.address.website
        })
    }

    ngOnInit() {
        this._address$ = new BehaviorSubject(this.address);
        this.address$ = this._address$.asObservable();
        this.organization$ = this.address$.pipe(
            map((address) => ({
                organizationType: this.organization.organizationType,
                name: this.organization.name,
                defaultAddress: address,
                addresses: [...this.organization.addresses, address]
            } as Organization))
        );

    }

}