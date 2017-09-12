import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {OrganisationService} from './organisation.service';
import Organisation from './organisation.model';
import {Answer} from '../shared/answer.model';
import {UrlParamBuilder} from '../url-param.builder';
import GeoPoint from './geopoint.model';
import {Observable} from 'rxjs/Observable';
import {ObservableUtil} from '../shared/observable.util';
import {Subject} from 'rxjs/Subject';

@Component({
    selector: 'organisation',
    templateUrl: './organisation.component.html',
    styleUrls: ['./organisation.component.scss'],
    providers: [OrganisationService]
})
export class OrganisationComponent implements OnInit {

    private _back$: Subject<void>;
    public organisation: Organisation;
    private userAnswers: Answer[];
    private organisations: Observable<Array<Organisation>>;
    private position: Observable<GeoPoint>;
    private distance: Observable<number>;
    private zoom: Observable<number>;

    constructor(private route: ActivatedRoute,
                private router: Router,
                private organisationService: OrganisationService) {
        this._back$ = <Subject<void>>new Subject();

        this.route.params.subscribe((params: Params) => {
            if (params.hasOwnProperty('answers')) {
                this.userAnswers = UrlParamBuilder.parseAnswers(params['answers']);
            }
        });

        this.position = ObservableUtil.extractObjectMember(this.route.params, 'position')
            .map(UrlParamBuilder.parseGeoPoint);
        this.distance = ObservableUtil.extractObjectMember(this.route.params, 'distance')
            .map(UrlParamBuilder.parseInt);
        this.organisations = ObservableUtil.extractObjectMember(this.route.params, 'organisation')
            .switchMap((id: string) => this.organisationService.getOrganisation(id))
            .map(organisation => [organisation]);

        this.zoom = Observable.from([12]);
        this.route.params
            .switchMap((params: Params) => this.organisationService.getOrganisation(params['organisation']))
            .subscribe((organisation: Organisation) => {
                this.organisation = organisation;
            });

        Observable.combineLatest(
            this.position,
            this.distance,
            this._back$.asObservable()
        )
            .subscribe(([position, distance]: [GeoPoint, number, void]) => {
                this.router.navigate(['/result', {
                    answers: UrlParamBuilder.buildAnswers(this.userAnswers),
                    position: UrlParamBuilder.buildGeoPoint(position),
                    distance: distance
                }]);
            })
    }

    ngOnInit(): void {
    }

}