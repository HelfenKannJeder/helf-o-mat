import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {HttpModule} from '@angular/http';
import {AppComponent} from './app.component';
import {appRoutingProviders, routing} from './app.routing';
import {OrganisationModule} from './organisation/organisation.module';
import {ResultModule} from './result/result.module';
import {QuestionModule} from './question/question.module';

@NgModule({
    declarations: [
        AppComponent
    ],
    imports: [
        QuestionModule,
        ResultModule,
        OrganisationModule,
        BrowserModule,
        FormsModule,
        HttpModule,
        routing,
    ],
    providers: [
        appRoutingProviders
    ],
    bootstrap: [
        AppComponent
    ]
})
export class AppModule {
}
