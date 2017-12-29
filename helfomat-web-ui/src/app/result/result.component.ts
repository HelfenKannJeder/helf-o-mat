import {Component, OnInit} from '@angular/core';
import {BoundingBox, Organisation, OrganisationService, UserAnswer} from '../_internal/resources/organisation.service';
import {Observable, Subject} from 'rxjs';
import {ActivatedRoute, NavigationExtras, Router} from '@angular/router';
import {UrlParamBuilder} from '../url-param.builder';
import {Answer} from '../shared/answer.model';
import {ObservableUtil} from '../shared/observable.util';
import {animate, state, style, transition, trigger} from '@angular/animations';
import {environment} from '../../environments/environment';
import {GeoPoint} from '../../_internal/geopoint';

@Component({
    selector: 'app-result',
    templateUrl: './result.component.html',
    styleUrls: ['./result.component.less'],
    providers: [OrganisationService],
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
    private _answers$: Subject<UserAnswer[]> = new Subject<UserAnswer[]>();
    public _position$: Subject<GeoPoint> = new Subject<GeoPoint>();
    public _boundingBox$: Subject<BoundingBox> = new Subject<BoundingBox>();
    public _zoom$: Subject<number> = new Subject<number>();
    public _organisation$: Subject<Organisation> = new Subject<Organisation>();
    public answers: Observable<Answer[]>;
    public position: Observable<GeoPoint>;
    public center: Observable<GeoPoint>;
    public distance = Observable.from([10]);
    public zoom: Observable<number>;

    // Outputs
    public organisations: Subject<Organisation[]> = new Subject<Organisation[]>();
    public clusteredOrganisations: Subject<GeoPoint[]> = new Subject<GeoPoint[]>();

    public visibleComponent: 'list' | 'question' = 'list';
    private explainScore: boolean = false;

    constructor(private organisationService: OrganisationService,
                private router: Router,
                private route: ActivatedRoute) {
        let position = Observable.merge(
            ObservableUtil.extractObjectMember(this.route.params, 'position')
                .map(UrlParamBuilder.parseGeoPoint),
            this._position$.asObservable() // should not be necessary if position is written to URL
        );

        this.position = position
            .debounceTime(100)
            .distinctUntilChanged();

        this.center = position
            .map((position) => {
                if (position == null) {
                    return environment.defaults.mapCenter;
                }
                return position;
            })
            .debounceTime(100)
            .distinctUntilChanged();

        this.zoom = Observable.concat(
            Observable.merge(
                Observable.of(environment.defaults.zoomLevel.withPosition),
                position
                    .filter(position => position == null)
                    .map(position => environment.defaults.zoomLevel.withoutPosition)
            ),
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
                    boundingBox: UrlParamBuilder.buildBoundingBox(boundingBox),
                    zoom: zoom
                }], {
                    replaceUrl: true
                });
            });

        Observable.combineLatest(
            this._answers$.asObservable(),
            this.position,
            this.distance
        )
            .flatMap(([answers, position, distance]: [UserAnswer[], GeoPoint, number]) => {
                if (answers == null && position == null) {
                    return this.organisationService.findGlobal();
                } else if (answers == null) {
                    return this.organisationService.findByPosition(position, distance);
                } else if (position == null) {
                    return this.organisationService.findGlobalByQuestionAnswers(answers);
                } else {
                    return this.organisationService.findByQuestionAnswersAndPosition(answers, position, distance);
                }
            })
            .subscribe((organisations) => {
                this.organisations.next(organisations);
            });

        Observable.combineLatest(
            this.position,
            this.distance,
            this._boundingBox$.asObservable(),
            this.zoom
        )
            .flatMap(([position, distance, boundingBox, zoom]: [GeoPoint, number, BoundingBox, number]) => {
                return this.organisationService.boundingBox(position, distance, boundingBox, zoom);
            })
            .subscribe((clusteredOrganisations: GeoPoint[]) => {
                this.clusteredOrganisations.next(clusteredOrganisations);
            });

        Observable.combineLatest(
            this._organisation$.asObservable(),
            this.answers,
            this.position,
            this.distance
        )
            .subscribe(([organisation, answers, position, distance]: [Organisation, Answer[], GeoPoint, number]) => {
                let extras: NavigationExtras = {};
                if (this.explainScore) {
                    extras.fragment = 'compare';
                }
                this.router.navigate(['/organisation/' + organisation.urlName, {
                    answers: UrlParamBuilder.buildAnswers(answers),
                    position: UrlParamBuilder.buildGeoPoint(position),
                    distance: distance,
                    scoreNorm: organisation.scoreNorm
                }], extras);
            });
    }

    updateOrganisations(answers: UserAnswer[]) {
        this._answers$.next(answers);
    }

    public openOrganisation(organisation: Organisation, explainScore: boolean = false): void {
        this.explainScore = explainScore;
        this._organisation$.next(organisation);
    }

}
