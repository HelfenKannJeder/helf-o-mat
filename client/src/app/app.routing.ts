import {ModuleWithProviders} from "@angular/core";
import {Routes, RouterModule} from "@angular/router";
import {QuestionComponent} from "./question/question.component";
import {ResultComponent} from "./result/result.component";
import {OrganisationComponent} from "./organisation/organisation.component";

const appRoutes: Routes = [
    { path: 'result', component: ResultComponent },
    { path: 'organisation', component: OrganisationComponent },
    { path: 'question', component: QuestionComponent },
    { path: '', component: QuestionComponent }
];

export const appRoutingProviders: any[] = [

];

export const routing: ModuleWithProviders = RouterModule.forRoot(appRoutes);