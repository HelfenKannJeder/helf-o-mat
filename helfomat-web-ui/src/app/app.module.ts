import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {AppComponent} from './app.component';
import {appRoutingProviders, routing} from './app.routing';
import {OrganisationModule} from './organisation/organisation.module';
import {ResultModule} from './result/result.module';
import {QuestionModule} from './question/question.module';
import {TranslateLoader, TranslateModule} from '@ngx-translate/core';
import {HttpClient, HttpClientModule} from '@angular/common/http';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {ManageModule} from "./manage/manage.module";
import {ToastrModule} from 'ngx-toastr';

export function createTranslateLoader(http: HttpClient) {
    return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}

@NgModule({
    declarations: [
        AppComponent
    ],
    imports: [
        QuestionModule,
        ResultModule,
        OrganisationModule,
        ManageModule,
        BrowserModule,
        BrowserAnimationsModule,
        FormsModule,
        HttpClientModule,
        routing,
        TranslateModule.forRoot({
            loader: {
                provide: TranslateLoader,
                useFactory: createTranslateLoader,
                deps: [HttpClient]
            }
        }),
        ToastrModule.forRoot({
            positionClass: 'toast-bottom-right',
            preventDuplicates: true
        })
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
