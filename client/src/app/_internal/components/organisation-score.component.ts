import {Component, Input} from '@angular/core';

@Component({
    selector: 'organisation-score',
    templateUrl: './organisation-score.component.html'
})
export class OrganisationScoreComponent {

    @Input()
    public scoreNorm: number;

}