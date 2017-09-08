import {HelfomatService} from "./helfomat.service";
import {Router, ActivatedRoute, Params} from "@angular/router";
import {Question} from "./question.model";
import {EventEmitter} from "@angular/core";
import UserAnswer from "../organisation/userAnswer.model";
import {Observable} from "rxjs";
import {Answer} from "../shared/answer.model";

export default class AbstractQuestionComponent {

    public organisations: EventEmitter<UserAnswer[]> = <EventEmitter<UserAnswer[]>>new EventEmitter();

    private showIndex: number = 0;
    public questions: Question[] = [];
    private userAnswers: Answer[] = [];
    protected unansweredQuestions: number[] = [];
    protected router: Router;
    protected route: ActivatedRoute;
    protected helfomatService: HelfomatService;

    constructor() {
    }

    ngOnInit(): void {
        this.showIndex = 0;
        Observable.combineLatest(
            this.helfomatService.findQuestions(),
            this.route.params
        )
        .subscribe(([questions, params]: [Question[], Params]) => {
            this.questions = questions;

            let numberOfAnswers: number = 0;
            if (params.hasOwnProperty('answers')) {
                this.userAnswers = JSON.parse(params['answers']);
                this.showIndex = this.userAnswers.length;

                let transmitAnswers: UserAnswer[] = [];
                this.userAnswers.forEach((answer, index) => {
                    if (this.questions[index] !== undefined) {
                        let id = this.questions[index].id;
                        transmitAnswers.push({id, answer});
                    }
                });
                numberOfAnswers = this.userAnswers.length;
                this.organisations.emit(transmitAnswers);
            }
            this.unansweredQuestions = Array(this.questions.length - numberOfAnswers).fill(0);
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

        let url = '/question';
        if (this.userAnswers.length == this.questions.length) {
            url = '/result';
        }
        this.router.navigate([url, {answers: JSON.stringify(this.userAnswers)}]);
    }
}