import {NgModule} from "@angular/core";
import {ChangePositionComponent} from "./change-position.component";
import {CommonModule} from "@angular/common";

@NgModule({
    declarations: [ChangePositionComponent],
    imports: [
        CommonModule
    ],
    exports: [ChangePositionComponent]
})
export class ChangePositionModule {
}