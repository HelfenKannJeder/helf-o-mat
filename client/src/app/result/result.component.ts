import {Component, OnInit} from '@angular/core';
import {SearchService} from './search.service';
import {Observable, Subject} from 'rxjs';
import {GeoPoint} from '../organisation/geopoint.model';
import {UserAnswer} from '../organisation/userAnswer.model';
import {BoundingBox} from '../organisation/boundingbox.model';
import {Organisation} from '../organisation/organisation.model';
import {ActivatedRoute, Router} from '@angular/router';
import {UrlParamBuilder} from '../url-param.builder';
import {Answer} from '../shared/answer.model';
import {ObservableUtil} from '../shared/observable.util';
import {animate, state, style, transition, trigger} from '@angular/animations';

@Component({
    selector: 'app-result',
    templateUrl: './result.component.html',
    styleUrls: ['./result.component.less'],
    providers: [SearchService],
    animations: [
        trigger('slide', [
            state('question', style({
                transform: 'translate3d(0, 0, 0)'
            })),
            state('list', style({
                transform: 'translate3d(-50%, 0, 0)'
            })),
            transition('list => question', animate('400ms ease-in-out')),
            transition('question => list', animate('400ms ease-in-out'))
        ])
    ]
})
export class ResultComponent implements OnInit {

    // Inputs
    private _answers$: Subject<UserAnswer[]>;
    public _position$: Subject<GeoPoint>;
    public _boundingBox$: Subject<BoundingBox>;
    public _zoom$: Subject<number>;
    public _organisation$: Subject<Organisation>;
    public answers: Observable<Answer[]>;
    public position: Observable<GeoPoint>;
    public distance = Observable.from([10]);
    public zoom: Observable<number>;

    // Outputs
    public organisations;
    public clusteredOrganisations;

    public visibleComponent = 'list';

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

        this.position = Observable.merge(
            Observable.from([new GeoPoint(49.009432, 8.403922)]),
            ObservableUtil.extractObjectMember(this.route.params, 'position')
                .map(UrlParamBuilder.parseGeoPoint),
            this._position$.asObservable() // should not be necessary if position is written to URL
        )
            .debounceTime(100)
            .distinctUntilChanged();

        this.zoom = Observable.concat(
            Observable.from([12]),
            this._zoom$.asObservable()
        )
            .debounceTime(100)
            .distinctUntilChanged();

        this.answers = ObservableUtil.extractObjectMember(this.route.params, 'answers')
            .map(UrlParamBuilder.parseAnswers);

    }

    ngOnInit() {
        Observable.combineLatest(
            this._answers$.asObservable(),
            this._position$.asObservable(),
            this.distance,
            this._boundingBox$.asObservable(),
            this._zoom$.asObservable()
        )
            .subscribe(([userAnswers, position, distance, boundingBox, zoom]: [UserAnswer[], GeoPoint, number, BoundingBox, number]) => {
                this.router.navigate(['/result', {
                    answers: UrlParamBuilder.buildAnswersFromUserAnswer(userAnswers),
                    position: UrlParamBuilder.buildGeoPoint(position),
                    distance: distance,
                    boundingBox: boundingBox,
                    zoom: zoom
                }]);
            });

        Observable.combineLatest(
            this._answers$.asObservable(),
            this.position,
            this.distance
        ).subscribe(([answer, position, distance]: [UserAnswer[], GeoPoint, number]) => {
            this.searchService.search(answer, position, distance);
        });

        Observable.combineLatest(
            this.position,
            this.distance,
            this._boundingBox$.asObservable(),
            this.zoom
        ).subscribe(([position, distance, boundingBox, zoom]: [GeoPoint, number, BoundingBox, number]) => {
            this.searchService.boundingBox(position, distance, boundingBox, zoom);
        });

        Observable.combineLatest(
            this._organisation$.asObservable(),
            this.answers,
            this.position,
            this.distance
        )
            .subscribe(([organisation, answers, position, distance]: [Organisation, Answer[], GeoPoint, number]) => {
                this.router.navigate(['/organisation', {
                    organisation: organisation.id,
                    answers: UrlParamBuilder.buildAnswers(answers),
                    position: UrlParamBuilder.buildGeoPoint(position),
                    distance: distance,
                    scoreNorm: organisation.scoreNorm
                }]);
            });
    }

    updateOrganisations(answers: UserAnswer[]) {
        this._answers$.next(answers);
    }

}
