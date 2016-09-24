import {HelfomatService} from "./helfomat.service";
import {Router, ActivatedRoute, Params} from "@angular/router";
import {Question} from "./question.model";

export default class AbstractQuestionComponent {

    private showIndex: number = 0;
    private questions: Question[] = [];
    private userAnswers: number[] = [];
    protected router: Router;
    protected route: ActivatedRoute;
    protected helfomatService: HelfomatService;

    ngOnInit(): void {
        this.showIndex = 0;
        this.route.params.forEach((params: Params) => {
            if (params.hasOwnProperty('answers')) {
                this.userAnswers = JSON.parse(params['answers']);
                this.showIndex = this.userAnswers.length;
            }
        });

        this.helfomatService.findQuestions().subscribe(q => this.questions = q);
    }

    getAnswerClasses(button: number, question: Question, conditionalClass: string): string[] {
        var classes = ['btn', 'btn-xs'];
        let answer = this.userAnswers[this.getNumberOfQuestion(question)];
        if (answer == button) {
            classes.push(conditionalClass);
        } else {
            classes.push('btn-default');
        }
        return classes;
    }

    getNumberOfQuestion(question: Question): number {
        return this.questions.indexOf(question);
    }

    answerQuestion(button: number, question: Question): void {
        this.userAnswers[this.getNumberOfQuestion(question)] = button;

        let url = '/question';
        if (this.userAnswers.length == this.questions.length) {
            url = '/result';
        }
        this.router.navigate([url, {answers: JSON.stringify(this.userAnswers)}]);
    }
}