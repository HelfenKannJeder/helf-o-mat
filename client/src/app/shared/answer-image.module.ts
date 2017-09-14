import {NgModule} from '@angular/core';
import {AnswerImagePipe} from './answer-image.pipe';

@NgModule({
    declarations: [
        AnswerImagePipe
    ],
    exports: [
        AnswerImagePipe
    ]
})
export class AnswerImageModule {
}