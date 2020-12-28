import {NgModule} from "@angular/core";
import {ConfirmEmailComponent} from "./confirm-email.component";
import {CommonModule} from "@angular/common";
import {TranslateModule} from "@ngx-translate/core";

@NgModule({
    declarations: [
        ConfirmEmailComponent
    ],
    imports: [
        CommonModule,
        TranslateModule
    ],
    exports: [
        ConfirmEmailComponent
    ]
})
export class ConfirmEmailModule {
}