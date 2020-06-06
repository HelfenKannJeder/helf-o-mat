import {Pipe, PipeTransform} from '@angular/core';
import {Answer} from './answer.model';

/*
 * Raise the value exponentially
 * Takes an answer and returns the path to the image
 * Usage:
 *   value | answerImage
 * Example:
 *   {{ Answer.YES |  answerImage}}
 *   formats to: assets/images/yes.png
 */
@Pipe({name: 'answerImage'})
export class AnswerImagePipe implements PipeTransform {
    transform(value: Answer, inactive: boolean = false): string {
        let addition = '';
        if (inactive === true) {
            addition = '-inactive';
        }
        return 'assets/images/' + Answer[value].toLowerCase() + addition + '.png';
    }
}