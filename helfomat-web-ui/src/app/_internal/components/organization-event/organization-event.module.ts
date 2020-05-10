import {NgModule} from '@angular/core';
import {OrganizationEventComponent} from "./organization-event.component";
import {CommonModule} from "@angular/common";
import {BrowserModule} from "@angular/platform-browser";
import {TextDiffModule} from "../text-diff/text-diff.module";
import {TranslateModule} from "@ngx-translate/core";
import {ChangePositionModule} from "../change-position/change-position.module";
import {TimeModule} from "../../../shared/time.module";
import {MapModule} from "../../../map/map.module";
import {AuthenticationModule} from "../../authentication/authentication.module";

@NgModule({
    declarations: [
        OrganizationEventComponent
    ],
    imports: [
        CommonModule,
        BrowserModule,
        TextDiffModule,
        TranslateModule,
        ChangePositionModule,
        TimeModule,
        MapModule,
        AuthenticationModule
    ],
    exports: [
        OrganizationEventComponent
    ]
})
export class OrganizationEventModule {
}