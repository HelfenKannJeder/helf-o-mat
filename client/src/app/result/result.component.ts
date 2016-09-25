import {Component, OnInit} from "@angular/core";
import {SearchService} from "./search.service";
import {Observable} from "rxjs";
import GeoPoint from "../organisation/geopoint.model";

@Component({
    selector: 'app-result',
    templateUrl: './result.component.html',
    styleUrls: ['./result.component.scss'],
    providers: [SearchService]
})
export class ResultComponent implements OnInit {

    private organisations;
    private position = <Observable<GeoPoint>>Observable.from([new GeoPoint(49.038883, 8.348804)]);
    private distance = Observable.from([10]);

    constructor(private searchService: SearchService) {
        this.organisations = searchService.organisations$;
    }

    ngOnInit() {
    }

    updateOrganisations(answers) {
        this.searchService.search(answers);
    }

}
