import {Component, OnInit} from "@angular/core";
import {ActivatedRoute, Params, Router} from "@angular/router";
import {OrganisationService} from "./organisation.service";
import Organisation from "./organisation.model";

@Component({
    selector: 'organisation',
    templateUrl: './organisation.component.html',
    styleUrls: ['./organisation.component.scss'],
    providers: [OrganisationService]
})
export class OrganisationComponent implements OnInit {

    private organisation: Organisation;
    private params: Params;

    constructor(private route: ActivatedRoute,
                private router: Router,
                private organisationService: OrganisationService) {
        this.route.params.subscribe((params: Params) => this.params = params);
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
            answers: this.params['answers']
        }]);
    }

}