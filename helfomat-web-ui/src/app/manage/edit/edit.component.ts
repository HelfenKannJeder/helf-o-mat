import {Component, OnInit} from "@angular/core";
import {ObservableUtil} from "../../shared/observable.util";
import {switchMap} from "rxjs/operators";
import {Group, Organisation, OrganisationService} from "../../_internal/resources/organisation.service";
import {ActivatedRoute} from "@angular/router";
import {Observable} from "rxjs";
import {CdkDragDrop, moveItemInArray} from '@angular/cdk/drag-drop';

@Component({
    templateUrl: './edit.component.html',
    styleUrls: [
        './edit.component.scss'
    ]
})
export class EditComponent implements OnInit {

    public organisation: Observable<Organisation>;

    constructor(
        private route: ActivatedRoute,
        private organisationService: OrganisationService
    ) {
        this.organisation = ObservableUtil.extractObjectMember(this.route.params, 'organisation')
            .pipe(
                switchMap((organisationName: string) => this.organisationService.getOrganisation(organisationName))
            );
        this.organisation.subscribe((organisation) => {
            console.log('organisation', organisation);
        })
    }

    drop(groups: Group[], event: CdkDragDrop<string[]>) {
        moveItemInArray(groups, event.previousIndex, event.currentIndex);
    }

    ngOnInit(): void {
    }

}