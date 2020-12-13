import {ModuleWithProviders} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {QuestionComponent} from './question/question.component';
import {ResultComponent} from './result/result.component';
import {OrganizationComponent} from './organization/organization.component';
import {KioskHomeComponent} from "./kiosk/home/kiosk-home.component";

const kioskRoutes: Routes = [
    {
        path: 'volunteer',
        children: [
            {path: 'organization/:organization', component: OrganizationComponent},
            {path: 'result', component: ResultComponent},
            {path: 'question', component: QuestionComponent},
            {path: '', redirectTo: 'question', pathMatch: 'full'}
        ]
    },
    {path: '', component: KioskHomeComponent, pathMatch: 'full'}
];

export const kioskRoutingProviders: any[] = [];

export const kioskRouting: ModuleWithProviders = RouterModule.forRoot(kioskRoutes);