import {Component, OnInit, ChangeDetectorRef} from "@angular/core";
import Organisation from "../organisation/organisation.model";
import {Observable} from "rxjs";
import {Input} from "@angular/core/src/metadata/directives";
import {Router, ActivatedRoute, Params} from "@angular/router";

@Component({
    selector: 'helfomat-list',
    templateUrl: './list.component.html',
    styleUrls: ['./list.component.scss']
})
export class ListComponent implements OnInit {

    @Input() organisations: Observable<Organisation[]>;

    private currentOrganisations: Organisation[] = [];
    private params: Params;

    constructor(private changeDetectorRef: ChangeDetectorRef,
                private router: Router,
                private route: ActivatedRoute) {
        this.route.params.subscribe(p => this.params = p);
    }

    ngOnInit() {
        this.organisations.subscribe((organisations: Organisation[]) => {
            this.currentOrganisations = organisations;
            this.changeDetectorRef.detectChanges();
        });
    }

    getImagePath(image: string): string {
        return "https://helfenkannjeder.de/uploads/pics/" + image;
    }

    openOrganisation(organisation: Organisation) {
        this.router.navigate(['/organisation', {
            organisation: organisation.id,
            answers: this.params['answers']
        }]);
    }

}
