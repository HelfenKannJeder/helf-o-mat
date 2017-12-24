import {Component, OnInit} from '@angular/core';
import {AbstractQuestionComponent} from './abstractQuestion.component';
import {HelfomatService} from './helfomat.service';
import {ActivatedRoute, Router} from '@angular/router';
import {ObservableUtil} from '../shared/observable.util';
import {Answer} from '../shared/answer.model';
import {Observable} from 'rxjs/Observable';
import {UrlParamBuilder} from '../url-param.builder';

@Component({
    selector: 'app-question',
    templateUrl: './question.component.html',
    styleUrls: ['./question.component.scss'],
    providers: [HelfomatService]
})
export class QuestionComponent extends AbstractQuestionComponent implements OnInit {
    constructor(protected router: Router,
                private route: ActivatedRoute,
                protected helfomatService: HelfomatService) {
        super();
    }

    getNavigateUrl(allQuestionsAnswered: boolean): string {
        if (allQuestionsAnswered) {
            return '/location';
        } else {
            return '/question';
        }
    }

    getAnswers(): Observable<Answer[]> {
        return Observable.concat(
            Observable.of(null),
            ObservableUtil.extractObjectMember<string>(this.route.params, 'answers')
                .map(userAnswers => UrlParamBuilder.parseAnswers(userAnswers))
        );
    }

}
