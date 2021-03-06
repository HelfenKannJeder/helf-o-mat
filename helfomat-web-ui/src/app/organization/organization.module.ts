import {NgModule} from '@angular/core';
import {OrganizationComponent} from './organization.component';
import {AnswerImageModule} from '../shared/answer-image.module';
import {BrowserModule} from '@angular/platform-browser';
import {OrganizationScoreModule} from '../_internal/components/organization-score.module';
import {TranslateModule} from '@ngx-translate/core';
import {MapModule} from '../map/map.module';
import {TimeComponent} from './unit/time.component';
import {CompareAnswerPipe} from './compare-answer.pipe';
import {TimeModule} from '../shared/time.module';
import {GroupNamesPipe} from './group-names.pipe';
import {NgxPageScrollCoreModule} from 'ngx-page-scroll-core';
import {NgxPageScrollModule} from 'ngx-page-scroll';
import {RouterModule} from "@angular/router";
import {DistanceModule} from "../_internal/components/distance/distance.module";
import {AuthenticationModule} from "../_internal/authentication/authentication.module";
import {QrCodeModule} from "../_internal/components/qr-code/qr-code.module";
import {ContactFormComponent} from "./_internal/contact-form.component";
import {FormsModule} from "@angular/forms";
import {RecaptchaV3Module} from "ng-recaptcha";
import {ContactFormConfirmComponent} from "./_internal/contact-form-confirm.component";

@NgModule({
    imports: [
        TranslateModule,
        MapModule,
        AnswerImageModule,
        BrowserModule,
        OrganizationScoreModule,
        TimeModule,
        NgxPageScrollCoreModule,
        NgxPageScrollModule,
        RouterModule,
        DistanceModule,
        AuthenticationModule,
        QrCodeModule,
        FormsModule,
        RecaptchaV3Module
    ],
    declarations: [
        OrganizationComponent,
        TimeComponent,
        CompareAnswerPipe,
        GroupNamesPipe,
        ContactFormComponent,
        ContactFormConfirmComponent
    ],
    exports: [
        OrganizationComponent
    ]
})
export class OrganizationModule {
}