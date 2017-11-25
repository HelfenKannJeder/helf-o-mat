import {Pipe, PipeTransform} from '@angular/core';
import {Answer, AnswerUtil} from '../shared/answer.model';

@Pipe({name: 'compareAnswer'})
export class CompareAnswerPipe implements PipeTransform {

    transform(organisationAnswer: Answer, userAnswer: Answer): any {
        if (organisationAnswer == userAnswer) {
            return '100';
        }
        if (AnswerUtil.areNeighbours(organisationAnswer, userAnswer)) {
            return '50';
        }
        return '0';
    }

}