import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {TextDiffComponent} from "./text-diff.component";

@NgModule({
    declarations: [
        TextDiffComponent
    ],
    imports: [
        CommonModule
    ],
    exports: [
        TextDiffComponent
    ]
})
export class TextDiffModule {
}