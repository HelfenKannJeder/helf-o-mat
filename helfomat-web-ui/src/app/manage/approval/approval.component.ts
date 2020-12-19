import {Component, OnInit} from "@angular/core";
import {ApprovalOverviewDto, ApprovalService} from "../../_internal/resources/approval.service";
import {Subject} from "rxjs";

@Component({
    templateUrl: './approval.component.html',
    styleUrls: ['./approval.component.scss']
})
export class ApprovalComponent implements OnInit {

    public approvals: Subject<ApprovalOverviewDto[]> = new Subject<ApprovalOverviewDto[]>();
    public approvalHistory: Subject<ApprovalOverviewDto[]> = new Subject<ApprovalOverviewDto[]>();

    constructor(
        private approvalService: ApprovalService
    ) {
    }

    ngOnInit(): void {
        this.approvalService.findAll()
            .subscribe(approvals => this.approvals.next(approvals));
        this.approvalService.findHistory()
            .subscribe(approvals => this.approvalHistory.next(approvals));
    }

}