import {Component, OnInit} from "@angular/core";
import AbstractQuestionComponent from "./abstractQuestion.component";
import {HelfomatService} from "./helfomat.service";
import {Router, ActivatedRoute} from "@angular/router";

@Component({
    selector: 'app-question',
    templateUrl: './question.component.html',
    styleUrls: ['./question.component.css'],
    providers: [HelfomatService]
})
export class QuestionComponent extends AbstractQuestionComponent implements OnInit {
    constructor(protected router: Router,
                protected route: ActivatedRoute,
                protected helfomatService: HelfomatService) {
        super();
    }

}
