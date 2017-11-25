import {ChangeDetectorRef, Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Organisation, PictureId} from '../organisation/organisation.model';
import {Observable} from 'rxjs';

@Component({
    selector: 'helfomat-list',
    templateUrl: './list.component.html',
    styleUrls: ['./list.component.scss']
})
export class ListComponent implements OnInit {

    @Input() organisations: Observable<Organisation[]>;

    @Output() openOrganisation: EventEmitter<Organisation> = new EventEmitter<Organisation>();

    @Output() openOrganisationScoreExplanation: EventEmitter<Organisation> = new EventEmitter<Organisation>();

    public currentOrganisations: Organisation[] = [];

    constructor(private changeDetectorRef: ChangeDetectorRef) {
    }

    ngOnInit() {
        this.organisations.subscribe((organisations: Organisation[]) => {
            this.currentOrganisations = organisations;
            this.changeDetectorRef.detectChanges();
        });
    }

    getImagePath(image: PictureId): string {
        return `api/picture/${image.value}/icon`;
    }

}
