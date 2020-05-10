import {Component, Input} from "@angular/core";
import {GeoPoint} from "../../_internal/geopoint";
import {Address, Organization} from "../_internal/resources/organization.service";
import {BehaviorSubject, Subject} from "rxjs";

@Component({
    selector: 'map-icon',
    templateUrl: './map-icon.component.html',
    styleUrls: ['./map-icon.component.scss']
})
export class MapIconComponent {

    @Input() private geoPoint: GeoPoint;
    @Input() private organizationType: string;

    public organization: Subject<Organization> = new BehaviorSubject<Organization>(null);

    ngOnInit() {
        const address: Address = {
            city: "",
            location: this.geoPoint,
            street: "",
            zipcode: ""
        }
        this.organization.next({
            addresses: [address],
            attendanceTimes: [],
            contactPersons: [],
            defaultAddress: address,
            description: "",
            groups: [],
            id: "",
            logo: undefined,
            name: "",
            organizationType: this.organizationType,
            questions: [],
            scoreNorm: null,
            urlName: "",
            volunteers: [],
            website: ""
        })
    }

}