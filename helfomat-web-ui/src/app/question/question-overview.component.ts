import {Component, EventEmitter, Input, OnDestroy, OnInit, Output} from '@angular/core';
import {AbstractQuestionComponent, QuestionWithUserAnswer} from './abstract-question.component';
import {HelfomatService} from './helfomat.service';
import {Router} from '@angular/router';
import {Observable, Subscription} from 'rxjs';
import {UserAnswer} from '../_internal/resources/organization.service';
import {map} from "rxjs/operators";

@Component({
    selector: 'app-question-overview',
    templateUrl: './question-overview.component.html',
    styleUrls: ['./question-overview.component.scss'],
    providers: [HelfomatService]
})
export class QuestionOverviewComponent extends AbstractQuestionComponent implements OnInit, OnDestroy {

    @Input() public currentAnswers: Observable<string>;
    @Output() public newAnswers: EventEmitter<string>;
    @Output() public questionUserAnswers: EventEmitter<Array<UserAnswer>> = new EventEmitter<Array<UserAnswer>>();

    private questionUserAnswersSubscription: Subscription;

    constructor(protected router: Router,
                protected helfomatService: HelfomatService) {
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
