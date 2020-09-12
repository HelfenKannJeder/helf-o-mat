import {NgModule} from '@angular/core';
import {MapComponent} from './map.component';
import {BrowserModule} from '@angular/platform-browser';
import {SingleMapComponent} from "./single-map.component";
import {AddressSearchComponent} from "./address-search.component";
import {MapIconComponent} from "./map-icon.component";
import {MapImplementationModule} from "../../environments/environment";

@NgModule({
    imports: [
        MapImplementationModule,
        BrowserModule
    ],
    declarations: [
        MapComponent,
        SingleMapComponent,
        AddressSearchComponent,
        MapIconComponent
    ],
    exports: [
        MapComponent,
        SingleMapComponent,
        AddressSearchComponent,
        MapIconComponent
    ]
})
export class MapModule {
}