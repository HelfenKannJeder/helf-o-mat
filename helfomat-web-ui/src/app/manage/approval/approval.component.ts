import {Component, OnInit} from "@angular/core";
import {ApprovalOverviewDto, ApprovalService} from "../../_internal/resources/approval.service";
import {combineLatest, Subject} from "rxjs";
import {ToastrService} from "ngx-toastr";
import {TranslateService} from "@ngx-translate/core";
import {OAuthService} from "angular-oauth2-oidc";
import {hasRole, Roles} from "../../_internal/authentication/util";
import {flatMap} from "rxjs/operators";

@Component({
    templateUrl: './approval.component.html',
    styleUrls: ['./approval.component.scss']
})
export class ApprovalComponent implements OnInit {

    public approvals: Subject<ApprovalOverviewDto[]> = new Subject<ApprovalOverviewDto[]>();

    constructor(
        private approvalService: ApprovalService,
        private translateService: TranslateService,
        private toastr: ToastrService,
        private oAuthService: OAuthService,
    ) {
    }

    ngOnInit(): void {
        this.approvalService.findAll()
            .subscribe(approvals => this.approvals.next(approvals));
    }

    approveAll(approvals: ApprovalOverviewDto[]) {
        combineLatest(
            approvals
                .map(approval => approval.approvalId)
                .map(approvalId => this.approvalService.confirmApproval(approvalId))
        )
            .pipe(flatMap(() => this.approvalService.findAll()))
            .subscribe((approvals) => {
                this.approvals.next(approvals);
                const message = this.translateService.instant('manage.organization.approval.success');
                this.toastr.success(message);
            });

    }

    isAdmin(): boolean {
        return hasRole(this.oAuthService.getAccessToken(), Roles.ADMIN);
    }

}