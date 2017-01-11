import {Component, OnInit, ChangeDetectorRef} from "@angular/core";
import Organisation from "../organisation/organisation.model";
import {Observable} from "rxjs";
import {Input} from "@angular/core/src/metadata/directives";

@Component({
    selector: 'helfomat-list',
    templateUrl: './list.component.html',
    styleUrls: ['./list.component.scss']
})
export class ListComponent implements OnInit {

    @Input() organisations: Observable<Organisation[]>;

    private currentOrganisations: Organisation[] = [];

    constructor(private changeDetectorRef: ChangeDetectorRef) {
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

}
