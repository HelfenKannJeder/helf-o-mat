import {Component} from "@angular/core";
import {ActivatedRoute, Router} from "@angular/router";
import {distinctUntilChanged, filter, flatMap, map, mergeMap} from "rxjs/operators";
import {ApprovalDetailDto, ApprovalId, ApprovalService} from "../../../_internal/resources/approval.service";
import {ObservableUtil} from "../../../shared/observable.util";
import {BehaviorSubject, of, Subject} from "rxjs";
import {TranslateService} from "@ngx-translate/core";
import {ToastrService} from "ngx-toastr";

@Component({
    templateUrl: './review.component.html',
    styleUrls: ['./review.component.scss']
})
export class ReviewComponent {

    public approval: Subject<ApprovalDetailDto> = new Subject<ApprovalDetailDto>();
    private doApprove: Subject<ApprovalId> = new BehaviorSubject(null);

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
            .pipe(flatMap((approvalId: string) => {
                return this.approvalService.findDetails({value: approvalId})
            }))
            .subscribe(approval => {
                this.approval.next(approval);
            })
        this.doApprove
            .pipe(
                distinctUntilChanged((id1, id2) => id1?.value === id2?.value),
                mergeMap((approvalId: ApprovalId) => {
                    if (approvalId == null) {
                        return of(null);
                    }
                    return this.approvalService.confirmApproval(approvalId)
                        .pipe(map(() => approvalId));
                }),
                filter(e => e != null)
            )
            .subscribe(() => {
                const message = this.translateService.instant('manage.organization.approval.success');
                this.toastr.success(message);
                this.router.navigate(["/admin/approval"])
            });
    }

    approve(approvalId: ApprovalId) {
        this.doApprove.next(approvalId);
    }
}