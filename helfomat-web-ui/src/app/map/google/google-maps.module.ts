import {NgModule} from '@angular/core';
import {GoogleMapsComponent} from './google-maps.component';
import {TranslateModule} from '@ngx-translate/core';
import {BrowserModule} from '@angular/platform-browser';

@NgModule({
    imports: [
        TranslateModule,
        BrowserModule
    ],
    declarations: [
        GoogleMapsComponent
    ],
    exports: [
        GoogleMapsComponent
    ]
})
export class GoogleMapsModule {
}