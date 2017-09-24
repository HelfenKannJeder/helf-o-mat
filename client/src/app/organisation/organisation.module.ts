import {NgModule} from '@angular/core';
import {OrganisationComponent} from './organisation.component';
import {AnswerImageModule} from '../shared/answer-image.module';
import {BrowserModule} from '@angular/platform-browser';
import {OrganisationScoreModule} from '../_internal/components/organisation-score.module';
import {TranslateModule} from '@ngx-translate/core';
import {MapModule} from '../map/map.module';

@NgModule({
    imports: [
        TranslateModule,
        MapModule,
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