import {Component, EventEmitter, Input, OnDestroy, OnInit, Output} from '@angular/core';
import {AbstractQuestionComponent, QuestionWithUserAnswer} from './abstract-question.component';
import {Router} from '@angular/router';
import {Observable, Subscription} from 'rxjs';
import {UserAnswer} from '../_internal/resources/organization.service';
import {map} from "rxjs/operators";
import {QuestionService} from "../_internal/resources/question.service";

@Component({
    selector: 'app-question-overview',
    templateUrl: './question-overview.component.html',
    styleUrls: ['./question-overview.component.scss']
})
export class QuestionOverviewComponent extends AbstractQuestionComponent implements OnInit, OnDestroy {

    @Input() public currentAnswers: Observable<string>;
    @Output() public newAnswers: EventEmitter<string>;
    @Output() public questionUserAnswers: EventEmitter<Array<UserAnswer>> = new EventEmitter<Array<UserAnswer>>();

    private questionUserAnswersSubscription: Subscription;

    constructor(protected router: Router,
                protected questionService: QuestionService) {
        super();
    }

    public ngOnInit(): void {
        super.ngOnInit();

        this.questionUserAnswersSubscription = this.questionWithUserAnswers
            .pipe(
                map((questionWithUserAnswers: Array<QuestionWithUserAnswer>) => {
                    let userAnswers = [];
                    for (let questionWithUserAnswer of questionWithUserAnswers) {
                        if (questionWithUserAnswer.userAnswer != null) {
                            userAnswers.push(questionWithUserAnswer.userAnswer);
                        }
                    }
                    return userAnswers;
                })
            )
            .subscribe((userAnswers: Array<UserAnswer>) => {
                this.questionUserAnswers.emit(userAnswers);
            });
    }

    public ngOnDestroy(): void {
        super.ngOnDestroy();

        this.questionUserAnswersSubscription.unsubscribe();
    }

    protected getCurrentAnswers(): Observable<string> {
        return this.currentAnswers;
    }
}
