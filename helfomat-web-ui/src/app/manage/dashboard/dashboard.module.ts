import {NgModule} from "@angular/core";
import {DashboardComponent} from "./dashboard.component";
import {RouterModule} from "@angular/router";

@NgModule({
    declarations: [
        DashboardComponent
    ],
    exports: [
        DashboardComponent
    ],
    imports: [
        RouterModule
    ]
})
export class DashboardModule {
}