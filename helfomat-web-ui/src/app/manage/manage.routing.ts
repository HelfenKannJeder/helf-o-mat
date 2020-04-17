import {Routes} from "@angular/router";
import {DashboardComponent} from "./dashboard/dashboard.component";
import {EditComponent} from "./edit/edit.component";
import {IsLoggedInGuard} from "../_internal/authentication/is-logged-in.guard";

export const manageRoutes: Routes = [
    {
        path: 'dashboard',
        component: DashboardComponent,
        canActivate: [IsLoggedInGuard]
    },
    {
        path: 'organization/:organization/edit',
        component: EditComponent,
        canActivate: [IsLoggedInGuard]
    }
];
