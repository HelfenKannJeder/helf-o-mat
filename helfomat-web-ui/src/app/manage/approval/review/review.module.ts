import {NgModule} from "@angular/core";
import {ReviewComponent} from "./review.component";
import {OrganizationEventModule} from "../../../_internal/components/organization-event/organization-event.module";
import {BrowserModule} from "@angular/platform-browser";
import {TranslateModule} from "@ngx-translate/core";
import {AutosizeModule} from "ngx-autosize";

@NgModule({
    declarations: [
        ReviewComponent
    ],
    imports: [
        OrganizationEventModule,
        BrowserModule,
        TranslateModule,
        AutosizeModule
    ],
    exports: [
        ReviewComponent
    ]
})
export class ReviewModule {
}