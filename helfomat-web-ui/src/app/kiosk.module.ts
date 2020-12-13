import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {OrganizationModule} from './organization/organization.module';
import {ResultModule} from './result/result.module';
import {QuestionModule} from './question/question.module';
import {TranslateLoader, TranslateModule} from '@ngx-translate/core';
import {HTTP_INTERCEPTORS, HttpClient, HttpClientModule} from '@angular/common/http';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {ToastrModule} from 'ngx-toastr';
import {ProfileModule} from "./profile/profile.module";
import {HttpErrorInterceptor} from "./_internal/http-error.interceptor";
import {KioskComponent} from "./kiosk.component";
import {kioskRouting, kioskRoutingProviders} from "./kiosk.routing";
import {KioskNavigateComponent} from "./kiosk/navigate/kiosk-navigate.component";
import {KioskHomeComponent} from "./kiosk/home/kiosk-home.component";

export function createTranslateLoader(http: HttpClient) {
    return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}

@NgModule({
    declarations: [
        KioskComponent,
        KioskHomeComponent,
        KioskNavigateComponent
    ],
    imports: [
        QuestionModule,
        ResultModule,
        OrganizationModule,
        BrowserModule,
        BrowserAnimationsModule,
        FormsModule,
        HttpClientModule,
        ProfileModule,
        kioskRouting,
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
        kioskRoutingProviders,
        {
            provide: HTTP_INTERCEPTORS,
            useClass: HttpErrorInterceptor,
            multi: true
        }
    ],
    bootstrap: [
        KioskComponent
    ]
})
export class KioskModule {
}
