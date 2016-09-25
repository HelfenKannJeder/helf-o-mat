import {Component, OnInit} from '@angular/core';
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

    constructor() {
    }

    ngOnInit() {
    }

    getImagePath(image: string): string {
        return "https://helfenkannjeder.de/uploads/pics/" + image;
    }

}
