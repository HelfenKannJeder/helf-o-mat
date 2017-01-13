import {Component, OnInit} from "@angular/core";
import {SearchService} from "./search.service";
import {Observable, Subject} from "rxjs";
import GeoPoint from "../organisation/geopoint.model";
import UserAnswer from "../organisation/userAnswer.model";
import BoundingBox from "../organisation/boundingbox.model";

@Component({
    selector: 'app-result',
    templateUrl: './result.component.html',
    styleUrls: ['./result.component.scss'],
    providers: [SearchService]
})
export class ResultComponent implements OnInit {

    // Inputs
    private _answers$: Subject<UserAnswer[]>;
    private _position$: Subject<GeoPoint>;
    private _boundingBox$: Subject<BoundingBox>;
    private _zoom$: Subject<number>;
    private position: Observable<GeoPoint>;
    private distance = Observable.from([10]);
    private zoom: Observable<number>;

    // Outputs
    private organisations;
    private clusteredOrganisations;

    constructor(private searchService: SearchService) {
        this._answers$ = <Subject<UserAnswer[]>>new Subject();
        this._position$ = <Subject<GeoPoint>>new Subject();
        this._boundingBox$ = <Subject<BoundingBox>>new Subject();
        this._zoom$ = <Subject<number>>new Subject();
        this.organisations = searchService.organisations$;
        this.clusteredOrganisations = searchService.clusteredOrganisations$;

        this.position = Observable.concat(
            Observable.from([new GeoPoint(49.009432, 8.403922)]),
            this._position$.asObservable()
        )
            .debounceTime(100)
            .distinctUntilChanged();

        this.zoom = Observable.concat(
            Observable.from([12]),
            this._zoom$.asObservable()
        )
            .debounceTime(100)
            .distinctUntilChanged();

    }

    ngOnInit() {
        Observable.combineLatest(
            this._answers$.asObservable(),
            this.position,
            this.distance
        ).subscribe(([answer, position, distance]:
            [UserAnswer[], GeoPoint, number]) => {
            this.searchService.search(answer, position, distance);
        });

        Observable.combineLatest(
            this.position,
            this.distance,
            this._boundingBox$.asObservable(),
            this.zoom
        ).subscribe(([position, distance, boundingBox, zoom]:
            [GeoPoint, number, BoundingBox, number]) => {
            this.searchService.boundingBox(position, distance, boundingBox, zoom);
        });
    }

    updateOrganisations(answers: UserAnswer[]) {
        this._answers$.next(answers);
    }

}
