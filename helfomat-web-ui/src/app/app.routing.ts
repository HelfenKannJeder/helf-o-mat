import {ModuleWithProviders} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {QuestionComponent} from './question/question.component';
import {ResultComponent} from './result/result.component';
import {OrganisationComponent} from './organisation/organisation.component';
import {NoAuthGuard} from "./_internal/authentication/no-auth.guard";

const appRoutes: Routes = [
    {path: 'result', component: ResultComponent, canActivate: [NoAuthGuard]},
    {path: 'organisation/:organisation', component: OrganisationComponent, canActivate: [NoAuthGuard]},
    {path: 'question', component: QuestionComponent, canActivate: [NoAuthGuard]},
    {path: 'location', component: ResultComponent, canActivate: [NoAuthGuard]},
    {path: '', redirectTo: '/question', pathMatch: 'full'}
];

export const appRoutingProviders: any[] = [];

export const routing: ModuleWithProviders = RouterModule.forRoot(appRoutes);