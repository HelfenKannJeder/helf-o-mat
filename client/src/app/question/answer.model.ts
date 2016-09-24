import {Question} from "./question.model";

export default class Answer {
    public question: Question;
    public answer: number;

    constructor(question: Question, answer: number) {
        this.question = question;
        this.answer = answer;
    }
}