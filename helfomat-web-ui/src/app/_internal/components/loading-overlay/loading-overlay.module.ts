import {NgModule} from "@angular/core";
import {LoadingOverlayComponent} from "./loading-overlay.component";
import {OverlayModule} from "@angular/cdk/overlay";

@NgModule({
    imports: [
        OverlayModule
    ],
    declarations: [
        LoadingOverlayComponent
    ],
    entryComponents: [
        LoadingOverlayComponent
    ]
})
export class LoadingOverlayModule {
}