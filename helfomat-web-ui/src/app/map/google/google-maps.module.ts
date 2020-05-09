import {NgModule} from '@angular/core';
import {GoogleMapsComponent} from './google-maps.component';
import {TranslateModule} from '@ngx-translate/core';
import {BrowserModule} from '@angular/platform-browser';
import {GoogleMapsAddressSearchComponent} from "./google-maps-address-search.component";

@NgModule({
    imports: [
        TranslateModule,
        BrowserModule
    ],
    declarations: [
        GoogleMapsComponent,
        GoogleMapsAddressSearchComponent
    ],
    exports: [
        GoogleMapsComponent,
        GoogleMapsAddressSearchComponent
    ]
})
export class GoogleMapsModule {
}