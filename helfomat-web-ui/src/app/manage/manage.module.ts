import {NgModule} from "@angular/core";
import {DashboardModule} from "./dashboard/dashboard.module";
import {EditModule} from "./edit/edit.module";
import {manageRoutes} from "./manage.routing";
import {RouterModule} from "@angular/router";

@NgModule({
    imports: [
        DashboardModule,
        EditModule,
        RouterModule.forChild(manageRoutes)
    ]
})
export class ManageModule {
}