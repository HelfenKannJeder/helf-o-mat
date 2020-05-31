import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {AppComponent} from './app.component';
import {appRoutingProviders, routing} from './app.routing';
import {OrganizationModule} from './organization/organization.module';
import {ResultModule} from './result/result.module';
import {QuestionModule} from './question/question.module';
import {TranslateLoader, TranslateModule} from '@ngx-translate/core';
import {HTTP_INTERCEPTORS, HttpClient, HttpClientModule} from '@angular/common/http';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {ManageModule} from "./manage/manage.module";
import {ToastrModule} from 'ngx-toastr';
import {OAuthModule} from "angular-oauth2-oidc";
import {ProfileModule} from "./profile/profile.module";
import {HttpErrorInterceptor} from "./_internal/http-error.interceptor";
import {AuthenticateModule} from "./authenticate/authenticate.module";

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
        OrganizationModule,
        ManageModule,
        BrowserModule,
        BrowserAnimationsModule,
        FormsModule,
        HttpClientModule,
        ProfileModule,
        AuthenticateModule,
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
        }),
        OAuthModule.forRoot({
            resourceServer: {
                sendAccessToken: true
            }
        })
    ],
    providers: [
        appRoutingProviders,
        {
            provide: HTTP_INTERCEPTORS,
            useClass: HttpErrorInterceptor,
            multi: true
        }
    ],
    bootstrap: [
        AppComponent
    ]
})
export class AppModule {
}
