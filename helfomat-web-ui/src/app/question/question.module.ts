import {NgModule} from '@angular/core';
import {AnswerImageModule} from '../shared/answer-image.module';
import {QuestionComponent} from './question.component';
import {QuestionOverviewComponent} from './questionOverview.component';
import {BrowserModule} from '@angular/platform-browser';
import {TranslateModule} from '@ngx-translate/core';

@NgModule({
    imports: [
        AnswerImageModule,
        BrowserModule,
        TranslateModule
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