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
import {CommonModule} from "@angular/common";
import {NgbModalModule, NgbTimepickerModule, NgbTypeaheadModule} from "@ng-bootstrap/ng-bootstrap";
import {OrganizationTemplateService} from "../../_internal/resources/organization-template.service";
import {DragDropDirective} from "./_internal/drag-drop.directive";
import {PublishChangesConfirmationComponent} from "./_internal/publish-changes-confirmation.component";
import {OrganizationEventModule} from "../../_internal/components/organization-event/organization-event.module";
import {ChangesSentForReviewComponent} from "./_internal/changes-sent-for-review.component";

@NgModule({
    providers: [
        OrganisationService,
        OrganizationTemplateService
    ],
    imports: [
        CommonModule,
        BrowserModule,
        TranslateModule,
        TimeModule,
        AnswerImageModule,
        FormsModule,
        AutosizeModule,
        DragDropModule,
        NgbTimepickerModule,
        NgbTypeaheadModule,
        NgbModalModule,
        OrganizationEventModule
    ],
    declarations: [
        EditComponent,
        DragDropDirective,
        PublishChangesConfirmationComponent,
        ChangesSentForReviewComponent
    ],
    entryComponents: [
        PublishChangesConfirmationComponent,
        ChangesSentForReviewComponent
    ]
})
export class EditModule {
}