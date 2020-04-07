import {NgModule} from '@angular/core';
import {OrganisationComponent} from './organisation.component';
import {AnswerImageModule} from '../shared/answer-image.module';
import {BrowserModule} from '@angular/platform-browser';
import {OrganisationScoreModule} from '../_internal/components/organisation-score.module';
import {TranslateModule} from '@ngx-translate/core';
import {MapModule} from '../map/map.module';
import {DistanceComponent} from './unit/distance.component';
import {TimeComponent} from './unit/time.component';
import {CompareAnswerPipe} from './compare-answer.pipe';
import {TimeModule} from '../shared/time.module';
import {GroupNamesPipe} from './group-names.pipe';
import {NgxPageScrollModule} from 'ngx-page-scroll';
import {RouterModule} from "@angular/router";

@NgModule({
    imports: [
        TranslateModule,
        MapModule,
        AnswerImageModule,
        BrowserModule,
        OrganisationScoreModule,
        TimeModule,
        NgxPageScrollModule,
        RouterModule
    ],
    declarations: [
        OrganisationComponent,
        DistanceComponent,
        TimeComponent,
        CompareAnswerPipe,
        GroupNamesPipe
    ],
    exports: [
        OrganisationComponent
    ]
})
export class OrganisationModule {
}