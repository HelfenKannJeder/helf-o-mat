import {NgModule} from "@angular/core";
import {DashboardModule} from "./dashboard/dashboard.module";
import {EditModule} from "./edit/edit.module";
import {manageRoutes} from "./manage.routing";
import {RouterModule} from "@angular/router";
import {ApprovalModule} from "./approval/approval.module";

@NgModule({
    imports: [
        DashboardModule,
        ApprovalModule,
        EditModule,
        RouterModule.forChild(manageRoutes)
    ]
})
export class ManageModule {
}