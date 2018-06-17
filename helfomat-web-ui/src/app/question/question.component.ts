import {Component, OnDestroy, OnInit} from '@angular/core';
import {AbstractQuestionComponent, QuestionWithUserAnswer} from './abstract-question.component';
import {HelfomatService} from './helfomat.service';
import {ActivatedRoute, Router} from '@angular/router';
import {ObservableUtil} from '../shared/observable.util';
import {Observable} from 'rxjs/Observable';
import {Subscription} from 'rxjs/Subscription';
import {Answer} from '../shared/answer.model';

@Component({
    selector: 'app-question',
    templateUrl: './question.component.html',
    styleUrls: ['./question.component.scss'],
    providers: [HelfomatService]
})
export class QuestionComponent extends AbstractQuestionComponent implements OnInit, OnDestroy {

    public questionWithUserAnswersSync: Array<QuestionWithUserAnswer>;

    private questionWithUserAnswersSubscription: Subscription;

    constructor(protected router: Router,
                private route: ActivatedRoute,
                protected helfomatService: HelfomatService) {
        super();

        Observable.combineLatest(
            this.newAnswers,
            this.questionWithUserAnswers
        )
            .subscribe(([answers, questionWithUserAnswers]: [string, Array<QuestionWithUserAnswer>]) => {
                let url = QuestionComponent.getNavigateUrl(this.getLastAnsweredQuestion(questionWithUserAnswers) == questionWithUserAnswers.length);
                this.router.navigate([url, {answers}]);
            })
    }

    public ngOnInit(): void {
        super.ngOnInit();

        this.questionWithUserAnswersSubscription = this.questionWithUserAnswers
            .subscribe((questionWithUserAnswersSync: Array<QuestionWithUserAnswer>) => {
                this.questionWithUserAnswersSync = questionWithUserAnswersSync;
            });
    }

    public ngOnDestroy(): void {
        super.ngOnDestroy();

        this.questionWithUserAnswersSubscription.unsubscribe();
    }

    public getLastAnsweredQuestion(questionWithUserAnswers: Array<QuestionWithUserAnswer>): number {
        let numQuestion: number = 0;
        for (let questionWithUserAnswer of questionWithUserAnswers) {
            if (questionWithUserAnswer.userAnswer == null) {
                return numQuestion;
            }
            numQuestion++;
        }
        return numQuestion;
    }

    public continueWithoutAnswers(): void {
        this.router.navigate([
            QuestionComponent.getNavigateUrl(true),
            {answers: null}
        ]);
    }

    public classOfAnswer(answer: Answer): string[] {
        if (answer == null) {
            return ['answer-missing'];
        }
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

    protected getCurrentAnswers(): Observable<string> {
        return Observable.concat(
            Observable.of(null),
            ObservableUtil.extractObjectMember<string>(this.route.params, 'answers')
        );
    }

    private static getNavigateUrl(allQuestionsAnswered: boolean): string {
        if (allQuestionsAnswered) {
            return '/location';
        } else {
            return '/question';
        }
    }

}
