import {NgModule} from '@angular/core';
import {TimePipe} from './time.pipe';

@NgModule({
    declarations: [
        TimePipe
    ],
    exports: [
        TimePipe
    ]
})
export class TimeModule {
}