import {Component, OnInit} from "@angular/core";
import {ApprovalOverviewDto, ApprovalService} from "../../_internal/resources/approval.service";
import {BehaviorSubject, combineLatest, of, Subject} from "rxjs";
import {ToastrService} from "ngx-toastr";
import {TranslateService} from "@ngx-translate/core";
import {OAuthService} from "angular-oauth2-oidc";
import {hasRole, Roles} from "../../_internal/authentication/util";
import {distinctUntilChanged, filter, map, mergeMap} from "rxjs/operators";

@Component({
    templateUrl: './approval.component.html',
    styleUrls: ['./approval.component.scss']
})
export class ApprovalComponent implements OnInit {

    public approvals: Subject<ApprovalOverviewDto[]> = new Subject<ApprovalOverviewDto[]>();
    private doApprove: Subject<ApprovalOverviewDto[]> = new BehaviorSubject(null);

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
        this.doApprove
            .pipe(
                distinctUntilChanged(),
                mergeMap((approvals: ApprovalOverviewDto[]) => {
                    if (approvals == null) {
                        return of(null);
                    }
                    return combineLatest(
                        approvals
                            .map(approval => approval.approvalId)
                            .map(approvalId => this.approvalService.confirmApproval(approvalId))
                    )
                        .pipe(map(() => approvals));
                }),
                filter(e => e != null),
                mergeMap(() => this.approvalService.findAll())
            )
            .subscribe((approvals) => {
                this.approvals.next(approvals);
                const message = this.translateService.instant('manage.organization.approval.success');
                this.toastr.success(message);
            });
    }

    approveAll(approvals: ApprovalOverviewDto[]) {
        this.doApprove.next(approvals);
    }

    isAdmin(): boolean {
        return hasRole(this.oAuthService.getAccessToken(), Roles.ADMIN);
    }

}