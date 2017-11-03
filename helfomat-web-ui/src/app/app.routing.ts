import {ModuleWithProviders} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {QuestionComponent} from './question/question.component';
import {ResultComponent} from './result/result.component';
import {OrganisationComponent} from './organisation/organisation.component';
import {LocationComponent} from './location/location.component';

const appRoutes: Routes = [
    {path: 'result', component: ResultComponent},
    {path: 'organisation', component: OrganisationComponent},
    {path: 'question', component: QuestionComponent},
    {path: 'location', component: LocationComponent},
    {path: '', redirectTo: '/question', pathMatch: 'full'}
];

export const appRoutingProviders: any[] = [];

export const routing: ModuleWithProviders = RouterModule.forRoot(appRoutes);