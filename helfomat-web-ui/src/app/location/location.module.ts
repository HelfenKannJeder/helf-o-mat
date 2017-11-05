import {NgModule} from '@angular/core';
import {LocationComponent} from './location.component';
import {MapModule} from '../map/map.module';
import {TranslateModule} from '@ngx-translate/core';

@NgModule({
    imports: [
        MapModule,
        TranslateModule
    ],
    declarations: [
        LocationComponent
    ],
    exports: [
        LocationComponent
    ]
})
export class LocationModule {
}