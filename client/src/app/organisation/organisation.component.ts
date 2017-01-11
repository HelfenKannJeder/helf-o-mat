import {Component, OnInit} from "@angular/core";
import {ActivatedRoute, Params, Router} from "@angular/router";

@Component({
    selector: 'organisation',
    templateUrl: './organisation.component.html',
    styleUrls: ['./organisation.component.scss']
})
export class OrganisationComponent implements OnInit {

    private organisation: string;
    private params: Params;

    constructor(private route: ActivatedRoute,
                private router: Router) {
        this.route.params.subscribe((params: Params) => {
            this.params = params;
            if (params.hasOwnProperty("organisation")) {
                this.organisation = params['organisation'];
            }
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