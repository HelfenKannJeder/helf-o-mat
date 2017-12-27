import {HelfomatService} from './helfomat.service';
import {Router} from '@angular/router';
import {Question} from './question.model';
import {EventEmitter} from '@angular/core';
import {Observable} from 'rxjs';
import 'rxjs/add/observable/combineLatest';
import {Answer} from '../shared/answer.model';
import {UserAnswer} from '../_internal/resources/organisation.service';

export abstract class AbstractQuestionComponent {

    public questionAnswers: EventEmitter<UserAnswer[]> = <EventEmitter<UserAnswer[]>>new EventEmitter();

    private indexOfCurrentQuestion: number = 0;
    public questions: Question[] = [];
    private userAnswers: Answer[] = [];
    protected unansweredQuestions: number[] = [];
    protected router: Router;
    protected helfomatService: HelfomatService;

    constructor() {
    }

    ngOnInit(): void {
        this.indexOfCurrentQuestion = 0;
        Observable.combineLatest(
            this.helfomatService.findQuestions(),
            this.getAnswers()
        )
            .subscribe(([questions, answers]: [Question[], Answer[]]) => {
                this.questions = questions;
                if (answers != null) {
                    this.userAnswers = answers;
                    this.indexOfCurrentQuestion = answers.length;
                    this.questionAnswers.emit(this.toUserAnswers(answers));
                } else {
                    this.questionAnswers.emit(null);
                }
                this.unansweredQuestions = Array(this.questions.length - this.indexOfCurrentQuestion).fill(0);
            });
    }

    isInactive(button: Answer, question: Question) {
        let answer = this.userAnswers[this.getNumberOfQuestion(question)];
        return answer !== button;
    }

    classOfAnswer(answer: Answer): string[] {
        switch (answer) {
            case Answer.NO:
                return ['answer-no'];
            case Answer.MAYBE:
                return ['answer-maybe'];
            case Answer.YES:
                return ['answer-yes'];
        }
        return [];
    }

    getNumberOfQuestion(question: Question): number {
        return this.questions.indexOf(question);
    }

    answerQuestion(answer: Answer, question: Question): void {
        this.userAnswers[this.getNumberOfQuestion(question)] = answer;

        let url = this.getNavigateUrl(this.userAnswers.length == this.questions.length);
        this.router.navigate([url, {answers: JSON.stringify(this.userAnswers)}]);
    }

    private toUserAnswers(userAnswers: Answer[]): UserAnswer[] {
        let transmitAnswers: UserAnswer[] = [];
        userAnswers.forEach((answer, index) => {
            if (this.questions[index] !== undefined) {
                let id = this.questions[index].id;
                transmitAnswers.push({id, answer});
            }
        });
        return transmitAnswers;
    }

    abstract getNavigateUrl(allQuestionsAnswered: boolean): string;

    abstract getAnswers(): Observable<Answer[]>;
}