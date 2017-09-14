import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
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

    public _back$: Subject<void>;
    public organisation: Observable<Organisation>;
    public userAnswers: Observable<Answer[]>;
    public organisations: Observable<Array<Organisation>>;
    public position: Observable<GeoPoint>;
    public center: Observable<GeoPoint>;
    public distance: Observable<number>;
    public zoom: Observable<number>;
    public scoreNorm: Observable<number>;

    constructor(private route: ActivatedRoute,
                private router: Router,
                private organisationService: OrganisationService) {
        this._back$ = <Subject<void>>new Subject();

        this.userAnswers = ObservableUtil.extractObjectMember(this.route.params, 'answers')
            .map(UrlParamBuilder.parseAnswers);
        this.position = ObservableUtil.extractObjectMember(this.route.params, 'position')
            .map(UrlParamBuilder.parseGeoPoint);
        this.distance = ObservableUtil.extractObjectMember(this.route.params, 'distance')
            .map(UrlParamBuilder.parseInt);
        this.scoreNorm = ObservableUtil.extractObjectMember(this.route.params, 'scoreNorm')
            .map(UrlParamBuilder.parseInt);
        this.organisation = ObservableUtil.extractObjectMember(this.route.params, 'organisation')
            .switchMap((id: string) => this.organisationService.getOrganisation(id));
        this.organisations = this.organisation.map(organisation => [organisation]);

        this.center = Observable
            .combineLatest(
                this.organisations,
                this.position
            )
            .map(([organisations, position]: [Organisation[], GeoPoint]) => {
                let location = OrganisationComponent.getOrganisaitonLocation(position, organisations[0]);
                return GeoPoint.pointBetween(position, location);
            });

        this.zoom = Observable
            .combineLatest(
                this.organisations,
                this.position
            )
            .map(([organisations, position]: [Organisation[], GeoPoint]) => {
                let location = OrganisationComponent.getOrganisaitonLocation(position, organisations[0]);
                return OrganisationComponent.calculateZoomLevel(location, position);
            });

        Observable.combineLatest(
            this.position,
            this.distance,
            this.userAnswers,
            this._back$.asObservable()
        )
            .subscribe(([position, distance, userAnswers]: [GeoPoint, number, Answer[], void]) => {
                this.router.navigate(['/result', {
                    answers: UrlParamBuilder.buildAnswers(userAnswers),
                    position: UrlParamBuilder.buildGeoPoint(position),
                    distance: distance
                }]);
            })
    }

    private static calculateZoomLevel(position1: GeoPoint, position2: GeoPoint) {
        let distanceInKm = GeoPoint.distanceInKm(position1, position2);
        let mapHeight = 200;
        let distanceInM = distanceInKm * 1000;
        let metersPerPixel = (distanceInM / mapHeight);
        let zoom = Math.floor(
            Math.log((156543.03392 * Math.cos(position2.lat * Math.PI / 180)) / metersPerPixel) / Math.log(2)
        );

        if (zoom <= 2) {
            zoom = 2;
        }
        if (zoom >= 20) {
            zoom = 20;
        }
        return zoom - 1;
    }

    private static getOrganisaitonLocation(position: GeoPoint, organisation: Organisation): GeoPoint {
        let distance = null;
        let location = null;
        for (let address of organisation.addresses) {
            let potentialDistance = GeoPoint.distanceInKm(position, address.location);
            if (distance === null || distance > potentialDistance) {
                distance = potentialDistance;
                location = address.location;
            }
        }
        return location;
    }

    ngOnInit(): void {
    }

}