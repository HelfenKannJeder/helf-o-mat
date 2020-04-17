import {AfterViewInit, Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Answer} from '../shared/answer.model';
import {UrlParamBuilder} from '../url-param.builder';
import {combineLatest, concat, Observable, of, Subject} from 'rxjs';
import {ObservableUtil} from '../shared/observable.util';
import {Address, Organization, OrganizationService, TravelDistance} from '../_internal/resources/organization.service';
import {GeoPoint} from '../../_internal/geopoint';
import {filter, flatMap, map, switchMap} from "rxjs/operators";

@Component({
    selector: 'organization',
    templateUrl: './organization.component.html',
    styleUrls: ['./organization.component.scss'],
    providers: [OrganizationService]
})
export class OrganizationComponent implements OnInit, AfterViewInit {

    public _back$: Subject<void>;
    public organization: Observable<Organization>;
    public userAnswers: Observable<Answer[]>;
    public organizations: Observable<Array<Organization>>;
    public position: Observable<GeoPoint>;
    public center: Observable<GeoPoint>;
    public distance: Observable<number>;
    public zoom: Observable<number>;
    public scoreNorm: Observable<number>;
    public travelDistances: Observable<Array<TravelDistance>>;
    private fragment: string;

    constructor(private route: ActivatedRoute,
                private router: Router,
                private organizationService: OrganizationService) {
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
        this.organization = ObservableUtil.extractObjectMember(this.route.params, 'organization')
            .pipe(
                switchMap((organizationName: string) => this.organizationService.getOrganization(organizationName))
            );
        this.organizations = this.organization.pipe(
            map(organization => [organization])
        );

        this.center = combineLatest(
            this.organizations,
            OrganizationComponent.prefixWithNull(this.position.pipe(filter(position => position != null)))
        )
            .pipe(
                map(([organizations, position]: [Organization[], GeoPoint]) => {
                    let location = OrganizationComponent.getOrganizationLocation(organizations[0]);
                    return GeoPoint.pointBetween(position, location);
                })
            );

        this.zoom = combineLatest(
            this.organizations,
            OrganizationComponent.prefixWithNull(this.position.pipe(filter(position => position != null)))
        )
            .pipe(
                map(([organizations, position]: [Organization[], GeoPoint]) => {
                    let location = OrganizationComponent.getOrganizationLocation(organizations[0]);
                    return OrganizationComponent.calculateZoomLevel(location, position);
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
            this.organization,
            this.position.pipe(
                filter(position => position != null)
            )
        )
            .pipe(
                flatMap(([organization, position]: [Organization, GeoPoint]) => this.organizationService.getTravelDistances(organization.id, position))
            );
    }

    public ngOnInit() {
        this.route.fragment.subscribe(fragment => {
            this.fragment = fragment;
        });
    }

    public ngAfterViewInit(): void {
        if (this.fragment !== undefined) {
            this.organization.subscribe(() => {
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
        return Address.isEqual(address1, address2);
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

    private static getOrganizationLocation(organization: Organization): GeoPoint {
        return organization.defaultAddress.location;
    }

    private static prefixWithNull<T>(observable: Observable<T>): Observable<T> {
        return concat(of(null), observable);
    }

}