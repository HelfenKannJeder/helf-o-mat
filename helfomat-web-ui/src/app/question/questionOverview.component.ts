import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {AbstractQuestionComponent} from './abstractQuestion.component';
import {HelfomatService} from './helfomat.service';
import {Router} from '@angular/router';
import {Observable} from 'rxjs/Observable';
import {Answer} from '../shared/answer.model';

@Component({
    selector: 'app-question-overview',
    templateUrl: './questionOverview.component.html',
    styleUrls: ['./questionOverview.component.scss'],
    providers: [HelfomatService]
})
export class QuestionOverviewComponent extends AbstractQuestionComponent implements OnInit {

    @Input() public answers: Observable<Answer[]>;
    @Output() public questionAnswers: EventEmitter<any> = new EventEmitter();

    constructor(protected router: Router,
                protected helfomatService: HelfomatService) {
        super();
    }

    getNavigateUrl(allQuestionsAnswered: boolean): string {
        return '/result';
    }

    getAnswers(): Observable<Answer[]> {
        return this.answers;
    }
}
