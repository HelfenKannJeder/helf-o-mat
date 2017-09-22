import {NgModule} from '@angular/core';
import {MapComponent} from './map.component';
import {TranslateModule} from '@ngx-translate/core';

@NgModule({
    imports: [
        TranslateModule
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