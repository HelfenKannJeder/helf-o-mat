import {AfterViewInit, Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Answer} from '../shared/answer.model';
import {UrlParamBuilder} from '../url-param.builder';
import {combineLatest, concat, Observable, of, Subject} from 'rxjs';
import {ObservableUtil} from '../shared/observable.util';
import {Address, Organisation, OrganisationService, TravelDistance} from '../_internal/resources/organisation.service';
import {GeoPoint} from '../../_internal/geopoint';
import {filter, flatMap, map, switchMap} from "rxjs/operators";

@Component({
    selector: 'organisation',
    templateUrl: './organisation.component.html',
    styleUrls: ['./organisation.component.scss'],
    providers: [OrganisationService]
})
export class OrganisationComponent implements OnInit, AfterViewInit {

    public _back$: Subject<void>;
    public organisation: Observable<Organisation>;
    public userAnswers: Observable<Answer[]>;
    public organisations: Observable<Array<Organisation>>;
    public position: Observable<GeoPoint>;
    public center: Observable<GeoPoint>;
    public distance: Observable<number>;
    public zoom: Observable<number>;
    public scoreNorm: Observable<number>;
    public travelDistances: Observable<Array<TravelDistance>>;
    private fragment: string;

    constructor(private route: ActivatedRoute,
                private router: Router,
                private organisationService: OrganisationService) {
        this._back$ = new Subject<void>();

        this.userAnswers = ObservableUtil.extractObjectMember(this.route.params, 'answers')
            .pipe(
                map(UrlParamBuilder.parseAnswers)
            );
        this.position = ObservableUtil.extractObjectMember(this.route.params, 'position')
            .pipe(
                map(UrlParamBuilder.parseGeoPoint)
            );
        this.distance = ObservableUtil.extractObjectMember(this.route.params, 'distance')
            .pipe(
                map(UrlParamBuilder.parseInt)
            );
        this.scoreNorm = ObservableUtil.extractObjectMember(this.route.params, 'scoreNorm')
            .pipe(
                map(UrlParamBuilder.parseInt)
            );
        this.organisation = ObservableUtil.extractObjectMember(this.route.params, 'organisation')
            .pipe(
                switchMap((organisationName: string) => this.organisationService.getOrganisation(organisationName))
            );
        this.organisations = this.organisation.pipe(
            map(organisation => [organisation])
        );

        this.center = combineLatest(
            this.organisations,
            OrganisationComponent.prefixWithNull(this.position.pipe(filter(position => position != null)))
        )
            .pipe(
                map(([organisations, position]: [Organisation[], GeoPoint]) => {
                    let location = OrganisationComponent.getOrganisationLocation(organisations[0]);
                    return GeoPoint.pointBetween(position, location);
                })
            );

        this.zoom = combineLatest(
            this.organisations,
            OrganisationComponent.prefixWithNull(this.position.pipe(filter(position => position != null)))
        )
            .pipe(
                map(([organisations, position]: [Organisation[], GeoPoint]) => {
                    let location = OrganisationComponent.getOrganisationLocation(organisations[0]);
                    return OrganisationComponent.calculateZoomLevel(location, position);
                })
            );

        combineLatest(
            this.position,
            ObservableUtil.extractObjectMember(this.route.params, 'zoom').pipe(map(UrlParamBuilder.parseInt)),
            this.distance,
            this.userAnswers,
            this._back$.asObservable()
        )
            .subscribe(([position, zoom, distance, userAnswers]: [GeoPoint, number, number, Answer[], void]) => {
                this.router.navigate(['/result', {
                    answers: UrlParamBuilder.buildAnswers(userAnswers),
                    position: UrlParamBuilder.buildGeoPoint(position),
                    zoom: zoom,
                    distance: distance
                }]);
            });

        this.travelDistances = combineLatest(
            this.organisation,
            this.position.pipe(
                filter(position => position != null)
            )
        )
            .pipe(
                flatMap(([organisation, position]: [Organisation, GeoPoint]) => this.organisationService.getTravelDistances(organisation.id, position))
            );
    }

    public ngOnInit() {
        this.route.fragment.subscribe(fragment => {
            this.fragment = fragment;
        });
    }

    public ngAfterViewInit(): void {
        if (this.fragment !== undefined) {
            this.organisation.subscribe(() => {
                window.setTimeout(() => {
                    let querySelector = document.querySelector('#' + this.fragment);
                    if (querySelector != null) {
                        querySelector.scrollIntoView();
                    }
                });
            });
        }
    }

    public areAddressesEqual(address1: Address, address2: Address): boolean {
        return address1.location.lon == address2.location.lon
            && address1.location.lat == address2.location.lat
            && address1.addressAppendix == address2.addressAppendix
            && address1.city == address2.city
            && address1.street == address2.street
            && address1.telephone == address2.telephone
            && address1.zipcode == address2.zipcode
            && address1.website == address2.website;
    }

    private static calculateZoomLevel(position1: GeoPoint, position2: GeoPoint): number {
        if (position1 == null || position2 == null) {
            return 12;
        }
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

    private static getOrganisationLocation(organisation: Organisation): GeoPoint {
        return organisation.defaultAddress.location;
    }

    private static prefixWithNull<T>(observable: Observable<T>): Observable<T> {
        return concat(of(null), observable);
    }

}