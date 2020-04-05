import {NgModule} from '@angular/core';
import {OrganizationEventComponent} from "./organization-event.component";
import {CommonModule} from "@angular/common";
import {BrowserModule} from "@angular/platform-browser";
import {TextDiffModule} from "../text-diff/text-diff.module";
import {TranslateModule} from "@ngx-translate/core";

@NgModule({
    declarations: [
        OrganizationEventComponent
    ],
    imports: [
        CommonModule,
        BrowserModule,
        TextDiffModule,
        TranslateModule
    ],
    exports: [
        OrganizationEventComponent
    ]
})
export class OrganizationEventModule {
}