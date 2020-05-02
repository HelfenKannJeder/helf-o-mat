import {Component, OnInit} from "@angular/core";
import {ApprovalOverviewDto, ApprovalService} from "../../_internal/resources/approval.service";
import {Subject} from "rxjs";

@Component({
    templateUrl: './approval.component.html'
})
export class ApprovalComponent implements OnInit {

    private approvals: Subject<ApprovalOverviewDto[]> = new Subject<>();

    constructor(private approvalService: ApprovalService) {
    }

    ngOnInit(): void {
        this.approvalService.findAll()
            .subscribe(approvals => this.approvals.next(approvals));
    }


}