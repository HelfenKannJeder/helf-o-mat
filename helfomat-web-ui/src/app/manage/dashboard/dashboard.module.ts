import {NgModule} from "@angular/core";
import {DashboardComponent} from "./dashboard.component";

@NgModule({
    declarations: [
        DashboardComponent
    ],
    exports: [
        DashboardComponent
    ]
})
export class DashboardModule {
}