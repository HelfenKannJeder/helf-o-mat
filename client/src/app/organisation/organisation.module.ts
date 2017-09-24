import {NgModule} from '@angular/core';
import {OrganisationComponent} from './organisation.component';
import {AnswerImageModule} from '../shared/answer-image.module';
import {BrowserModule} from '@angular/platform-browser';
import {OrganisationScoreModule} from '../_internal/components/organisation-score.module';
import {TranslateModule} from '@ngx-translate/core';
import {AlternativeMapModule} from '../map/alternative/alternative-map.module';

@NgModule({
    imports: [
        TranslateModule,
        AlternativeMapModule,
        AnswerImageModule,
        BrowserModule,
        OrganisationScoreModule
    ],
    declarations: [
        OrganisationComponent
    ],
    exports: [
        OrganisationComponent
    ]
})
export class OrganisationModule {
}