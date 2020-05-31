import {NgModule} from "@angular/core";
import {CreateOrganizationDialogComponent} from "./create-organization-dialog.component";
import {TranslateModule} from "@ngx-translate/core";
import {FormsModule} from "@angular/forms";
import {CommonModule} from "@angular/common";
import {CreateOrganizationDialogService} from "./create-organization-dialog.service";

@NgModule({
    declarations: [
        CreateOrganizationDialogComponent
    ],
    imports: [
        TranslateModule,
        FormsModule,
        CommonModule
    ],
    providers: [
        CreateOrganizationDialogService
    ]
})
export class CreateOrganizationDialogModule {
}