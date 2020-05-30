import {Routes} from "@angular/router";
import {DashboardComponent} from "./dashboard/dashboard.component";
import {EditComponent} from "./edit/edit.component";
import {IsLoggedInGuard} from "../_internal/authentication/is-logged-in.guard";
import {ApprovalComponent} from "./approval/approval.component";
import {IsReviewerGuard} from "../_internal/authentication/is-reviewer.guard";
import {ReviewComponent} from "./approval/review/review.component";

export const manageRoutes: Routes = [
    {
        path: 'admin',
        canActivate: [IsReviewerGuard],
        children: [
            {
                path: 'dashboard',
                component: DashboardComponent
            },
            {
                path: 'approval',
                children: [
                    {
                        path: '',
                        component: ApprovalComponent,
                        pathMatch: 'full'
                    },
                    {
                        path: 'review/:approvalId',
                        component: ReviewComponent
                    }
                ]
            },
            {path: '', redirectTo: 'approval', pathMatch: 'full'}
        ]
    },
    {
        path: 'volunteer/organization/:organization/edit',
        component: EditComponent,
        canActivate: [IsLoggedInGuard]
    },
    {
        path: 'volunteer/organization/:organizationType/create',
        component: EditComponent,
        canActivate: [IsLoggedInGuard]
    }
];
