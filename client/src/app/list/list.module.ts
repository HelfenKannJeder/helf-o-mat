import {NgModule} from '@angular/core';
import {ListComponent} from './list.component';
import {BrowserModule} from '@angular/platform-browser';
import {OrganisationScoreModule} from '../_internal/components/organisation-score.module';

@NgModule({
    imports: [
        BrowserModule,
        OrganisationScoreModule
    ],
    declarations: [
        ListComponent
    ],
    exports: [
        ListComponent
    ]
})
export class ListModule {
}