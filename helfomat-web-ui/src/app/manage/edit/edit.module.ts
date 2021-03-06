import {NgModule} from "@angular/core";
import {EditComponent} from "./edit.component";
import {BrowserModule} from "@angular/platform-browser";
import {TranslateModule} from "@ngx-translate/core";
import {OrganizationService} from "../../_internal/resources/organization.service";
import {TimeModule} from "../../shared/time.module";
import {AnswerImageModule} from "../../shared/answer-image.module";
import {FormsModule} from "@angular/forms";
import {AutosizeModule} from "ngx-autosize";
import {DragDropModule} from '@angular/cdk/drag-drop';
import {CommonModule} from "@angular/common";
import {NgbModalModule, NgbNavModule, NgbTimepickerModule, NgbTypeaheadModule} from "@ng-bootstrap/ng-bootstrap";
import {OrganizationTemplateService} from "../../_internal/resources/organization-template.service";
import {DragDropDirective} from "./_internal/drag-drop.directive";
import {PublishChangesConfirmationComponent} from "./_internal/publish-changes-confirmation.component";
import {OrganizationEventModule} from "../../_internal/components/organization-event/organization-event.module";
import {ChangesSentForReviewComponent} from "./_internal/changes-sent-for-review.component";
import {MapModule} from "../../map/map.module";
import {EditAddressComponent} from "./_internal/edit-address.component";
import {GeoCoordinateComponent} from "./_internal/geo-coordinate.component";
import {PictureService} from "../../_internal/resources/picture.service";
import {AuthenticationModule} from "../../_internal/authentication/authentication.module";
import {Ng2ImgMaxModule} from "ng2-img-max";
import {EditContactPersonComponent} from "./_internal/edit-contact-person.component";
import {FileUploadComponent} from "./_internal/file-upload.component";
import {EditLogoComponent} from "./_internal/edit-logo.component";
import {LoadingOverlayModule} from "../../_internal/components/loading-overlay/loading-overlay.module";

@NgModule({
    providers: [
        OrganizationService,
        PictureService,
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
        OrganizationEventModule,
        MapModule,
        NgbNavModule,
        AuthenticationModule,
        Ng2ImgMaxModule,
        LoadingOverlayModule
    ],
    declarations: [
        EditComponent,
        DragDropDirective,
        PublishChangesConfirmationComponent,
        ChangesSentForReviewComponent,
        EditAddressComponent,
        GeoCoordinateComponent,
        EditContactPersonComponent,
        FileUploadComponent,
        EditLogoComponent
    ],
    entryComponents: [
        PublishChangesConfirmationComponent,
        ChangesSentForReviewComponent,
        EditAddressComponent,
        EditContactPersonComponent,
        EditLogoComponent
    ]
})
export class EditModule {
}