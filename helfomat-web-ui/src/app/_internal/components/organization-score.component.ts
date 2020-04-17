import {Component, Input} from '@angular/core';

@Component({
    selector: 'organization-score',
    templateUrl: './organization-score.component.html'
})
export class OrganizationScoreComponent {

    @Input()
    public scoreNorm: number;

}