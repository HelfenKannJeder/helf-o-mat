import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {AbstractQuestionComponent} from './abstractQuestion.component';
import {HelfomatService} from './helfomat.service';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
    selector: 'app-question-overview',
    templateUrl: './questionOverview.component.html',
    styleUrls: ['./questionOverview.component.scss'],
    providers: [HelfomatService]
})
export class QuestionOverviewComponent extends AbstractQuestionComponent implements OnInit {

    @Output() public organisations: EventEmitter<any> = new EventEmitter();

    constructor(protected router: Router,
                protected route: ActivatedRoute,
                protected helfomatService: HelfomatService) {
        super();
    }

}
