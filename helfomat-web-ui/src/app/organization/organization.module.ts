import {NgModule} from '@angular/core';
import {OrganizationComponent} from './organization.component';
import {AnswerImageModule} from '../shared/answer-image.module';
import {BrowserModule} from '@angular/platform-browser';
import {OrganizationScoreModule} from '../_internal/components/organization-score.module';
import {TranslateModule} from '@ngx-translate/core';
import {MapModule} from '../map/map.module';
import {DistanceComponent} from './unit/distance.component';
import {TimeComponent} from './unit/time.component';
import {CompareAnswerPipe} from './compare-answer.pipe';
import {TimeModule} from '../shared/time.module';
import {GroupNamesPipe} from './group-names.pipe';
import {NgxPageScrollCoreModule} from 'ngx-page-scroll-core';
import {RouterModule} from "@angular/router";

@NgModule({
    imports: [
        TranslateModule,
        MapModule,
        AnswerImageModule,
        BrowserModule,
        OrganizationScoreModule,
        TimeModule,
        NgxPageScrollCoreModule,
        RouterModule
    ],
    declarations: [
        OrganizationComponent,
        DistanceComponent,
        TimeComponent,
        CompareAnswerPipe,
        GroupNamesPipe
    ],
    exports: [
        OrganizationComponent
    ]
})
export class OrganizationModule {
}