import {NgModule} from '@angular/core';
import {MapModule} from '../map/map.module';
import {ResultComponent} from './result.component';
import {ListModule} from '../list/list.module';
import {BrowserModule} from '@angular/platform-browser';
import {QuestionModule} from '../question/question.module';
import {TranslateModule} from '@ngx-translate/core';
import {RouterModule} from "@angular/router";
import {CreateOrganizationDialogModule} from "../_internal/components/create-organization-dialog/create-organization-dialog.module";
import {QrCodeModule} from "../_internal/components/qr-code/qr-code.module";

@NgModule({
    imports: [
        BrowserModule,
        TranslateModule,
        QuestionModule,
        MapModule,
        ListModule,
        RouterModule,
        CreateOrganizationDialogModule,
        QrCodeModule
    ],
    declarations: [
        ResultComponent
    ],
    exports: [
        ResultComponent
    ]
})
export class ResultModule {
}