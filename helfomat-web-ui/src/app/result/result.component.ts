import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {BoundingBox, Organisation, OrganisationService, UserAnswer} from '../_internal/resources/organisation.service';
import {BehaviorSubject, combineLatest, concat, from, merge, Observable, of, Subject} from 'rxjs';
import {ActivatedRoute, NavigationExtras, Router} from '@angular/router';
import {UrlParamBuilder} from '../url-param.builder';
import {ObservableUtil} from '../shared/observable.util';
import {animate, state, style, transition, trigger} from '@angular/animations';
import {environment} from '../../environments/environment';
import {GeoPoint} from '../../_internal/geopoint';
import {debounceTime, distinctUntilChanged, filter, first, flatMap, map} from "rxjs/operators";

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
    public _newAnswers$: Subject<string> = new Subject<string>();
    public answers: Observable<string>;
    public position: Observable<GeoPoint>;
    public center: Observable<GeoPoint>;
    public distance = from([10]);
    public zoom: Observable<number>;
    public _mapSize$: Subject<string> = new BehaviorSubject<string>('normal');
    public mapSize: Observable<string>;
    public hasPosition: boolean = true;

    // Outputs
    public organisations: Subject<Organisation[]> = new Subject<Organisation[]>();
    public clusteredOrganisations: Subject<GeoPoint[]> = new Subject<GeoPoint[]>();

    public visibleComponent: 'list' | 'question' = 'list';
    private explainScore: boolean = false;

    constructor(private organisationService: OrganisationService,
                private router: Router,
                private route: ActivatedRoute,
                private changeDetectorRef: ChangeDetectorRef) {
        let position: Observable<GeoPoint> = merge(
            ObservableUtil.extractObjectMember(this.route.params, 'position')
                .pipe(
                    map(UrlParamBuilder.parseGeoPoint)
                ),
            this._position$.asObservable() // should not be necessary if position is written to URL
        );

        this.position = position
            .pipe(
                debounceTime(100),
                distinctUntilChanged()
            );

        position
            .pipe(
                filter(position => position !== null),
                first()
            )
            .subscribe(() => {
                this.hasPosition = true;
                this._mapSize$.next('normal');
                this._zoom$.next(environment.defaults.zoomLevel.withPosition);
                this.changeDetectorRef.detectChanges();
            });

        this.center = position
            .pipe(
                map((position) => {
                    if (position == null) {
                        return environment.defaults.mapCenter;
                    }
                    return position;
                }),
                debounceTime(100),
                distinctUntilChanged()
            );

        this.zoom = concat(
            of(environment.defaults.zoomLevel.withoutPosition),
            this._zoom$.asObservable()
        )
            .pipe(
                debounceTime(100),
                distinctUntilChanged()
            );

        this.answers = ObservableUtil.extractObjectMember(this.route.params, 'answers');

        ObservableUtil.extractObjectMember(this.route.params, 'mapSize')
            .pipe(
                filter(mapSize => mapSize != null)
            )
            .subscribe((mapSize: string) => {
                if (mapSize === 'fullscreen') {
                    this.hasPosition = false;
                }
                return this._mapSize$.next(mapSize);
            });
        this.mapSize = this._mapSize$.asObservable();
    }

    ngOnInit() {
        combineLatest(
            this._answers$.asObservable(),
            merge(of(null), this._position$.asObservable()),
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

        combineLatest(
            this._answers$.asObservable(),
            this.position,
            this.distance
        )
            .pipe(
                flatMap(([answers, position, distance]: [Array<UserAnswer>, GeoPoint, number]) => {
                    if (answers.length == 0 && position == null) {
                        return this.organisationService.findGlobal();
                    } else if (answers.length == 0) {
                        return this.organisationService.findByPosition(position, distance);
                    } else if (position == null) {
                        return this.organisationService.findGlobalByQuestionAnswers(answers);
                    } else {
                        return this.organisationService.findByQuestionAnswersAndPosition(answers, position, distance);
                    }
                })
            )
            .subscribe((organisations) => {
                this.organisations.next(organisations);
            });

        combineLatest(
            this.position,
            this.distance,
            this._boundingBox$.asObservable(),
            this.zoom
        )
            .pipe(
                flatMap(([position, distance, boundingBox, zoom]: [GeoPoint, number, BoundingBox, number]) => {
                    return this.organisationService.boundingBox(position, distance, boundingBox, zoom);
                })
            )
            .subscribe((clusteredOrganisations: GeoPoint[]) => {
                this.clusteredOrganisations.next(clusteredOrganisations);
            });

        combineLatest(
            this._organisation$.asObservable(),
            this._newAnswers$.asObservable(),
            this.position,
            this.distance
        )
            .subscribe(([organisation, answers, position, distance]: [Organisation, string, GeoPoint, number]) => {
                let extras: NavigationExtras = {};
                if (this.explainScore) {
                    extras.fragment = 'compare';
                }
                this.router.navigate(['/organisation/' + organisation.urlName, {
                    answers: answers,
                    position: UrlParamBuilder.buildGeoPoint(position),
                    distance: distance,
                    scoreNorm: organisation.scoreNorm
                }], extras);
            });
    }

    public continueWithoutLocation(mapSize?: string): void {
        if (!this.hasPosition) {
            this.hasPosition = true;
            if (mapSize !== 'normal') {
                this._mapSize$.next('normal');
            }
            this.changeDetectorRef.detectChanges();
        }
    }

    updateOrganisations(answers: UserAnswer[]) {
        this._answers$.next(answers);
    }

    public openOrganisation(organisation: Organisation, explainScore: boolean = false): void {
        this.explainScore = explainScore;
        this._organisation$.next(organisation);
    }

}
