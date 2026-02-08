import {Component, OnDestroy, OnInit} from '@angular/core';
import {AbstractQuestionComponent, QuestionWithUserAnswer} from './abstract-question.component';
import {ActivatedRoute, Router} from '@angular/router';
import {ObservableUtil} from '../shared/observable.util';
import {combineLatest, concat, Observable, of, Subscription} from 'rxjs';
import {Answer} from '../shared/answer.model';
import {environment} from "../../environments/environment";
import {Question, QuestionService} from "../_internal/resources/question.service";
import {AnalyticsService} from "../_internal/analytics.service";

@Component({
    selector: 'app-question',
    templateUrl: './question.component.html',
    styleUrls: ['./question.component.scss']
})
export class QuestionComponent extends AbstractQuestionComponent implements OnInit, OnDestroy {

    public questionWithUserAnswersSync: Array<QuestionWithUserAnswer>;

    private questionWithUserAnswersSubscription: Subscription;

    constructor(
        protected router: Router,
        private route: ActivatedRoute,
        protected questionService: QuestionService,
        private analyticsService: AnalyticsService
    ) {
        super();

        combineLatest([
            this.newAnswers,
            this.questionWithUserAnswers
        ])
            .subscribe(([answers, questionWithUserAnswers]: [string, Array<QuestionWithUserAnswer>]) => {
                const lastAnsweredQuestion = this.getLastAnsweredQuestion(questionWithUserAnswers);
                let url = QuestionComponent.getNavigateUrl(lastAnsweredQuestion == questionWithUserAnswers.length && lastAnsweredQuestion != 0);
                this.router.navigate([url, {answers, position: null, mapSize: 'fullscreen'}]);
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

    public answerQuestion(answer: Answer, question: Question): void {
        const idx = this.getLastAnsweredQuestion(this.questionWithUserAnswersSync);
        const total = this.questionWithUserAnswersSync.length;
        this.analyticsService.trackEvent('helfomat-question-answered', {questionIndex: idx, totalQuestions: total});
        if (idx === total - 1) {
            this.analyticsService.trackEvent('helfomat-questionnaire-completed', {totalQuestions: total});
        }
        super.answerQuestion(answer, question);
    }

    public continueWithoutQuestions(): void {
        this.analyticsService.trackEvent('helfomat-questionnaire-skipped');
        this.router.navigate([
            QuestionComponent.getNavigateUrl(true),
            {answers: null, position: null, mapSize: 'fullscreen'}
        ]);
    }

    public classOfAnswer(answer: Answer): string[] {
        if (answer == null) {
            return ['material-icons', 'answer', 'answer-missing'];
        }
        switch (answer) {
            case Answer.NO:
                return ['material-icons', 'answer', 'answer-no'];
            case Answer.MAYBE:
                return ['material-icons', 'answer', 'answer-maybe'];
            case Answer.YES:
                return ['material-icons', 'answer', 'answer-yes'];
        }
        console.error('unexpected answer for question', answer);
        return ['material-icons', 'answer', 'answer-missing'];
    }

    public showSkipQuestions(): boolean {
        return !environment.kiosk;
    }

    protected getCurrentAnswers(): Observable<string> {
        return concat(
            of(null),
            ObservableUtil.extractObjectMember<string>(this.route.params, 'answers')
        );
    }

    private static getNavigateUrl(allQuestionsAnswered: boolean): string {
        if (allQuestionsAnswered) {
            return '/helf-o-mat/volunteer/result';
        } else {
            return '/helf-o-mat/volunteer/question';
        }
    }

}
