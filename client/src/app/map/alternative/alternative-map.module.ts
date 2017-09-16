import {NgModule} from '@angular/core';
import {AlternativeMapComponent} from './alternative-map.component';
import {BrowserModule} from '@angular/platform-browser';

@NgModule({
    imports: [
        BrowserModule
    ],
    declarations: [
        AlternativeMapComponent
    ],
    exports: [
        AlternativeMapComponent
    ]
})
export class AlternativeMapModule {
}