import {NgModule} from '@angular/core';
import {ListComponent} from './list.component';
import {BrowserModule} from '@angular/platform-browser';

@NgModule({
    imports: [
        BrowserModule
    ],
    declarations: [
        ListComponent
    ],
    exports: [
        ListComponent
    ]
})
export class ListModule {
}