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
    private _position$: Subject<GeoPoint>;
    private position = <Observable<GeoPoint>>Observable.from([new GeoPoint(49.009432, 8.403922)]);
    private distance = Observable.from([10]);

    // Outputs
    private organisations;

    constructor(private searchService: SearchService) {
        this._answers$ = <Subject<Answer[]>>new Subject();
        this._position$ = <Subject<GeoPoint>>new Subject();
        this.organisations = searchService.organisations$;

    }

    ngOnInit() {
        let positionObservable = Observable.concat(this.position, this._position$.asObservable())
            .debounceTime(100)
            .distinctUntilChanged();

        Observable.combineLatest(
            this._answers$.asObservable(),
            positionObservable,
            this.distance
        ).subscribe((searchParams: [Answer[], GeoPoint, number]) => {
            this.searchService.search(searchParams[0], searchParams[1], searchParams[2]);
        });
    }

    updateOrganisations(answers: Answer[]) {
        this._answers$.next(answers);
    }

}
