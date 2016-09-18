import {Component, OnInit} from '@angular/core';
import {Question} from './question.model';
import {HelfomatService} from './helfomat.service';

@Component({
    selector: 'app-question',
    templateUrl: './question.component.html',
    styleUrls: ['./question.component.css'],
    providers: [HelfomatService]
})
export class QuestionComponent implements OnInit {
    private questions: Question[] = [];

    constructor(private helfomatService: HelfomatService) {
    }

    ngOnInit() {
        this.helfomatService.findQuestions().subscribe(
            q => this.questions = q
        );
    }

}
