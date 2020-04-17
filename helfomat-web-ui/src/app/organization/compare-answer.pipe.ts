import {Pipe, PipeTransform} from '@angular/core';
import {Answer, AnswerUtil} from '../shared/answer.model';

@Pipe({name: 'compareAnswer'})
export class CompareAnswerPipe implements PipeTransform {

    transform(organizationAnswer: Answer, userAnswer: Answer): any {
        if (organizationAnswer == userAnswer) {
            return '100';
        }
        if (AnswerUtil.areNeighbours(organizationAnswer, userAnswer)) {
            return '50';
        }
        return '0';
    }

}