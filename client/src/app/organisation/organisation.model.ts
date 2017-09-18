import {Address} from './address.model';
import {Group} from './group.model';
import {AnsweredQuestion} from './answeredQuestion.model';

export class Organisation {
    public id: string;
    public name: string;
    public description: string;
    public website: string;
    public scoreNorm: number;
    public mapPin: string;
    public addresses: Address[] = [];
    public groups: Group[] = [];
    public questions: AnsweredQuestion[] = [];
    public logo: string;
}