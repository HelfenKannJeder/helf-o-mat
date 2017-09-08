import {Component, OnInit} from "@angular/core";
import {ActivatedRoute, Params, Router} from "@angular/router";
import {OrganisationService} from "./organisation.service";
import Organisation from "./organisation.model";
import {Answer} from "../shared/answer.model";
import {UrlParamBuilder} from '../url-param.builder';
import GeoPoint from './geopoint.model';
import {Observable} from 'rxjs/Observable';

@Component({
    selector: 'organisation',
    templateUrl: './organisation.component.html',
    styleUrls: ['./organisation.component.scss'],
    providers: [OrganisationService]
})
export class OrganisationComponent implements OnInit {

    public organisation: Organisation;
    private userAnswers: Answer[];
    private position: GeoPoint;
    private distance: number;
    private zoom: Observable<number>;

    constructor(private route: ActivatedRoute,
                private router: Router,
                private organisationService: OrganisationService) {
        this.route.params.subscribe((params: Params) => {
            if (params.hasOwnProperty('answers')) {
                this.userAnswers = UrlParamBuilder.parseAnswers(params['answers']);
            }
            if (params.hasOwnProperty('position')) {
                this.position = UrlParamBuilder.parseGeoPoint(params['position']);
            }
            if (params.hasOwnProperty('distance')) {
                this.distance = UrlParamBuilder.parseInt(params['distance']);
            }
        });
        this.zoom = Observable.from([12]);
        this.route.params
            .switchMap((params: Params) => this.organisationService.getOrganisation(params['organisation']))
            .subscribe((organisation: Organisation) => {
                this.organisation = organisation;
            });
    }

    ngOnInit(): void {
    }

    back(): void {
        this.router.navigate(['/result', {
            answers: UrlParamBuilder.buildAnswers(this.userAnswers),
            position: UrlParamBuilder.buildGeoPoint(this.position),
            distance: this.distance
        }]);
    }

}