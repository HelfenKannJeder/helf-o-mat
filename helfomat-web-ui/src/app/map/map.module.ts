import {NgModule} from '@angular/core';
import {MapComponent} from './map.component';
import {TranslateModule} from '@ngx-translate/core';
import {BrowserModule} from '@angular/platform-browser';

@NgModule({
    imports: [
        TranslateModule,
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