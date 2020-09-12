import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {NoneMapComponent} from "./none-map.component";

@NgModule({
    imports: [
        BrowserModule
    ],
    declarations: [
        NoneMapComponent
    ],
    exports: [
        NoneMapComponent
    ]
})
export class NoneMapModule {
}