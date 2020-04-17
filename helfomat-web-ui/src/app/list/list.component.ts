import {ChangeDetectorRef, Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Observable} from 'rxjs';
import {Organization, PictureId} from '../_internal/resources/organization.service';

@Component({
    selector: 'helfomat-list',
    templateUrl: './list.component.html',
    styleUrls: ['./list.component.scss']
})
export class ListComponent implements OnInit {

    @Input() organizations: Observable<Organization[]>;

    @Output() openOrganization: EventEmitter<Organization> = new EventEmitter<Organization>();

    @Output() openOrganizationScoreExplanation: EventEmitter<Organization> = new EventEmitter<Organization>();

    public currentOrganizations: Organization[] = [];

    constructor(private changeDetectorRef: ChangeDetectorRef) {
    }

    ngOnInit() {
        this.organizations.subscribe((organizations: Organization[]) => {
            this.currentOrganizations = organizations;
            this.changeDetectorRef.detectChanges();
        });
    }

    getImagePath(image: PictureId): string {
        return `api/picture/${image.value}/icon`;
    }

}
