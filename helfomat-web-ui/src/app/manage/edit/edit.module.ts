import {NgModule} from "@angular/core";
import {EditComponent} from "./edit.component";
import {BrowserModule} from "@angular/platform-browser";
import {TranslateModule} from "@ngx-translate/core";
import {OrganisationService} from "../../_internal/resources/organisation.service";
import {TimeModule} from "../../shared/time.module";
import {AnswerImageModule} from "../../shared/answer-image.module";
import {FormsModule} from "@angular/forms";
import {AutosizeModule} from "ngx-autosize";
import {DragDropModule} from '@angular/cdk/drag-drop';

@NgModule({
    providers: [
        OrganisationService
    ],
    imports: [
        BrowserModule,
        TranslateModule,
        TimeModule,
        AnswerImageModule,
        FormsModule,
        AutosizeModule,
        DragDropModule
    ],
    declarations: [
        EditComponent
    ]
})
export class EditModule {
}