import {Component, OnInit} from "@angular/core";
import {SearchService} from "./search.service";
import {Observable, Subject} from "rxjs";
import GeoPoint from "../organisation/geopoint.model";
import UserAnswer from "../organisation/userAnswer.model";
import BoundingBox from "../organisation/boundingbox.model";
import Organisation from "../organisation/organisation.model";
import {Router, ActivatedRoute, Params} from "@angular/router";

@Component({
    selector: 'app-result',
    templateUrl: './result.component.html',
    styleUrls: ['./result.component.scss'],
    providers: [SearchService]
})
export class ResultComponent implements OnInit {

    // Inputs
    private _answers$: Subject<UserAnswer[]>;
    public _position$: Subject<GeoPoint>;
    public _boundingBox$: Subject<BoundingBox>;
    public _zoom$: Subject<number>;
    public _organisation$: Subject<Organisation>;
    public position: Observable<GeoPoint>;
    public distance = Observable.from([10]);
    public zoom: Observable<number>;

    // Outputs
    public organisations;
    public clusteredOrganisations;

    constructor(private searchService: SearchService,
                private router: Router,
                private route: ActivatedRoute) {
        this._answers$ = <Subject<UserAnswer[]>>new Subject();
        this._position$ = <Subject<GeoPoint>>new Subject();
        this._boundingBox$ = <Subject<BoundingBox>>new Subject();
        this._zoom$ = <Subject<number>>new Subject();
        this._organisation$ = <Subject<Organisation>>new Subject();
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

        Observable.combineLatest(
            this._organisation$.asObservable(),
            this.route.params
        )
            .subscribe(([organisation, params]: [Organisation, Params]) => {
                this.router.navigate(['/organisation', {
                    organisation: organisation.id,
                    answers: params['answers']
                }]);
            });
    }

    updateOrganisations(answers: UserAnswer[]) {
        this._answers$.next(answers);
    }

}
