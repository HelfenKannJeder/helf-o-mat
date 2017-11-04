import {NgModule} from '@angular/core';
import {AlternativeMapModule} from './alternative/alternative-map.module';
import {GoogleMapsModule} from './google/google-maps.module';
import {MapComponent} from './map.component';
import {BrowserModule} from '@angular/platform-browser';

@NgModule({
    imports: [
        AlternativeMapModule,
        GoogleMapsModule,
        BrowserModule
    ],
    declarations: [
        MapComponent
    ],
    exports: [
        MapComponent
    ]
})
export class MapModule {
}