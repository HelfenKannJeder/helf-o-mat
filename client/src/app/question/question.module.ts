import {NgModule} from '@angular/core';
import {AnswerImageModule} from '../shared/answer-image.module';
import {QuestionComponent} from './question.component';
import {QuestionOverviewComponent} from './questionOverview.component';
import {BrowserModule} from '@angular/platform-browser';

@NgModule({
    imports: [
        AnswerImageModule,
        BrowserModule
    ],
    declarations: [
        QuestionComponent,
        QuestionOverviewComponent
    ],
    exports: [
        QuestionComponent,
        QuestionOverviewComponent
    ]
})
export class QuestionModule {
}