import {NgModule} from '@angular/core';
import {AlternativeMapModule} from './alternative/alternative-map.module';
import {GoogleMapsModule} from './google/google-maps.module';
import {MapComponent} from './map.component';
import {BrowserModule} from '@angular/platform-browser';
import {SingleMapComponent} from "./single-map.component";
import {AddressSearchComponent} from "./address-search.component";

@NgModule({
    imports: [
        AlternativeMapModule,
        GoogleMapsModule,
        BrowserModule
    ],
    declarations: [
        MapComponent,
        SingleMapComponent,
        AddressSearchComponent
    ],
    exports: [
        MapComponent,
        SingleMapComponent,
        AddressSearchComponent
    ]
})
export class MapModule {
}