import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {NoneMapComponent} from "./none-map.component";
import {NoneAddressSearchComponent} from "./none-address-search.component";

@NgModule({
    imports: [
        BrowserModule
    ],
    declarations: [
        NoneMapComponent,
        NoneAddressSearchComponent
    ],
    exports: [
        NoneMapComponent,
        NoneAddressSearchComponent
    ]
})
export class NoneMapModule {
}