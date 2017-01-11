import {Component, OnInit} from "@angular/core";
import {SearchService} from "./search.service";
import {Observable, Subject} from "rxjs";
import GeoPoint from "../organisation/geopoint.model";
import Answer from "../organisation/answer.model";
import BoundingBox from "../organisation/boundingbox.model";

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
    private _boundingBox$: Subject<BoundingBox>;
    private _zoom$: Subject<number>;
    private position: Observable<GeoPoint>;
    private distance = Observable.from([10]);
    private zoom: Observable<number>;

    // Outputs
    private organisations;
    private clusteredOrganisations;

    constructor(private searchService: SearchService) {
        this._answers$ = <Subject<Answer[]>>new Subject();
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
        // TODO: Split in two separate calls for circle and for bounding box / zoom
        Observable.combineLatest(
            this._answers$.asObservable(),
            this.position,
            this.distance,
            this._boundingBox$.asObservable(),
            this.zoom
        ).subscribe(([answer, position, distance, boundingBox, zoom]:
            [Answer[], GeoPoint, number, BoundingBox, number]) => {
            this.searchService.search(answer, position, distance, boundingBox, zoom);
        });
    }

    updateOrganisations(answers: Answer[]) {
        this._answers$.next(answers);
    }

}
