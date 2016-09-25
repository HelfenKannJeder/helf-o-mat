import {Component, OnInit} from "@angular/core";
import {SearchService} from "./search.service";
import {Observable, Subject} from "rxjs";
import GeoPoint from "../organisation/geopoint.model";
import Answer from "../organisation/answer.model";

@Component({
    selector: 'app-result',
    templateUrl: './result.component.html',
    styleUrls: ['./result.component.scss'],
    providers: [SearchService]
})
export class ResultComponent implements OnInit {

    // Inputs
    private _answers$: Subject<Answer[]>;
    private position = <Observable<GeoPoint>>Observable.from([new GeoPoint(49.009432, 8.403922)]);
    private distance = Observable.from([10]);

    // Outputs
    private organisations;

    constructor(private searchService: SearchService) {
        this._answers$ = <Subject<Answer[]>>new Subject();
        this.organisations = searchService.organisations$;

    }

    ngOnInit() {
        Observable.combineLatest(
            this._answers$.asObservable(),
            this.position,
            this.distance
        ).subscribe((searchParams: [Answer[], GeoPoint, number]) => {
            console.log(searchParams);
            this.searchService.search(searchParams[0], searchParams[1], searchParams[2]);
        });
    }

    updateOrganisations(answers: Answer[]) {
        this._answers$.next(answers);
    }

}
