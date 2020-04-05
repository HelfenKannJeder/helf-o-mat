import {Component, Input} from "@angular/core";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
    selector: 'changes-sent-for-review',
    templateUrl: './changes-sent-for-review.component.html'
})
export class ChangesSentForReviewComponent {

    @Input()
    public organizationName: string;

    constructor(public modal: NgbActiveModal) {
    }

}