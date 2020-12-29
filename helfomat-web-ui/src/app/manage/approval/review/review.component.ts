import {Component} from "@angular/core";
import {ActivatedRoute, Router} from "@angular/router";
import {distinctUntilChanged, filter, map, mergeMap} from "rxjs/operators";
import {ApprovalDetailDto, ApprovalId, ApprovalService} from "../../../_internal/resources/approval.service";
import {ObservableUtil} from "../../../shared/observable.util";
import {BehaviorSubject, of, Subject} from "rxjs";
import {TranslateService} from "@ngx-translate/core";
import {ToastrService} from "ngx-toastr";
import {OrganizationEvent} from "../../../_internal/resources/organization.service";

@Component({
    templateUrl: './review.component.html',
    styleUrls: ['./review.component.scss']
})
export class ReviewComponent {

    public approval: Subject<ApprovalDetailDto> = new Subject<ApprovalDetailDto>();
    private doApprove: Subject<{ approvalId: ApprovalId, changes: OrganizationEvent[] }> = new BehaviorSubject(null);
    private organizationEvents: Array<OrganizationEvent> = [];

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private approvalService: ApprovalService,
        private translateService: TranslateService,
        private toastr: ToastrService
    ) {
    }

    ngOnInit(): void {
        ObservableUtil.extractObjectMember(this.route.params, 'approvalId')
            .pipe(mergeMap((approvalId: string) => {
                return this.approvalService.findDetails({value: approvalId})
            }))
            .subscribe(approval => {
                this.organizationEvents = approval.proposedDomainEvent.changes;
                this.approval.next(approval);
            })
        this.doApprove
            .pipe(
                distinctUntilChanged((approval1, approval2) => approval1?.approvalId?.value === approval2?.approvalId?.value),
                mergeMap((approval) => {
                    if (approval == null) {
                        return of(null);
                    }
                    const {approvalId, changes} = approval;
                    return this.approvalService.confirmApproval(approvalId, this.organizationEvents)
                        .pipe(map(() => this.organizationEvents.length !== 0));
                }),
                filter(e => e != null)
            )
            .subscribe((isApprove) => {
                if (isApprove) {
                    this.toastr.success(
                        this.translateService.instant(
                            'manage.organization.approval.success'
                        )
                    );
                } else {
                    this.toastr.warning(
                        this.translateService.instant(
                            'manage.organization.decline.success'
                        )
                    );
                }
                this.router.navigate(["/admin/approval"])
            });
    }

    approve(approval: ApprovalDetailDto) {
        this.doApprove.next({
            approvalId: approval.approvalId,
            changes: approval.proposedDomainEvent.changes
        });
    }

    decline(approval: ApprovalDetailDto) {
        const result = window.confirm(this.translateService.instant('manage.organization.approval.confirmDecline'));
        if (result) {
            this.doApprove.next({
                approvalId: approval.approvalId,
                changes: []
            });
        }
    }

    public enabledOrganizations(organizationEvents: Array<OrganizationEvent>) {
        this.organizationEvents = organizationEvents;
    }

    public isPartialApprove(approval: ApprovalDetailDto) {
        return approval.proposedDomainEvent.changes.length != this.organizationEvents.length
            && this.organizationEvents.length > 0;
    }

    public isCompleteApprove(approval: ApprovalDetailDto) {
        return approval.proposedDomainEvent.changes.length == this.organizationEvents.length;
    }

}