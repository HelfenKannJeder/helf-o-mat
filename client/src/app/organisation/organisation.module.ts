import {NgModule} from '@angular/core';
import {OrganisationComponent} from './organisation.component';
import {MapModule} from '../map/map.module';
import {AnswerImageModule} from '../shared/answer-image.module';
import {BrowserModule} from '@angular/platform-browser';

@NgModule({
    imports: [
        MapModule,
        AnswerImageModule,
        BrowserModule
    ],
    declarations: [
        OrganisationComponent
    ],
    exports: [
        OrganisationComponent
    ]
})
export class OrganisationModule {
}