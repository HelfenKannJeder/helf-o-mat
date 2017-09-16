import {NgModule} from '@angular/core';
import {MapModule} from '../map/map.module';
import {ResultComponent} from './result.component';
import {ListModule} from '../list/list.module';
import {BrowserModule} from '@angular/platform-browser';
import {QuestionModule} from '../question/question.module';

@NgModule({
    imports: [
        BrowserModule,
        QuestionModule,
        MapModule,
        ListModule
    ],
    declarations: [
        ResultComponent
    ],
    exports: [
        ResultComponent
    ]
})
export class ResultModule {
}