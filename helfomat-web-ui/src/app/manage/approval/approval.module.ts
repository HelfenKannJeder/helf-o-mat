import {NgModule} from "@angular/core";
import {ApprovalService} from "../../_internal/resources/approval.service";
import {BrowserModule} from "@angular/platform-browser";
import {TranslateModule} from "@ngx-translate/core";
import {ApprovalComponent} from "./approval.component";
import {FormsModule} from "@angular/forms";
import {ReviewModule} from "./review/review.module";
import {RouterModule} from "@angular/router";

@NgModule({
    declarations: [
        ApprovalComponent
    ],
    exports: [
        ApprovalComponent
    ],
    imports: [
        ReviewModule,
        BrowserModule,
        TranslateModule,
        FormsModule,
        RouterModule
    ],
    providers: [
        ApprovalService
    ]
})
export class ApprovalModule {
}