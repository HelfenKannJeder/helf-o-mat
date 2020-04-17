import {NgModule} from '@angular/core';
import {ListComponent} from './list.component';
import {BrowserModule} from '@angular/platform-browser';
import {OrganizationScoreModule} from '../_internal/components/organization-score.module';
import {TranslateModule} from '@ngx-translate/core';

@NgModule({
    imports: [
        BrowserModule,
        OrganizationScoreModule,
        TranslateModule
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