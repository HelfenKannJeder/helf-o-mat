import {Component, OnInit} from "@angular/core";
import {SearchService} from "./search.service";

@Component({
    selector: 'app-result',
    templateUrl: './result.component.html',
    styleUrls: ['./result.component.scss'],
    providers: [SearchService]
})
export class ResultComponent implements OnInit {

    private organisations;

    constructor(private searchService: SearchService) {
        this.organisations = searchService.organisations$;
    }

    ngOnInit() {
    }

    updateOrganisations(event) {
        this.searchService.search();
    }

}
