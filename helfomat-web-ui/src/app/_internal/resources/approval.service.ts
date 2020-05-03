import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Organization, OrganizationEvent, OrganizationId} from "./organization.service";

@Injectable()
export class ApprovalService {

    constructor(private httpClient: HttpClient) {
    }

    findAll(): Observable<ApprovalOverviewDto[]> {
        return this.httpClient.get<ApprovalOverviewDto[]>('api/approval');
    }

    findDetails(approvalId: ApprovalId): Observable<ApprovalDetailDto> {
        return this.httpClient.get<ApprovalDetailDto>(`api/approval/${approvalId.value}`);
    }

    confirmApproval(approvalId: ApprovalId): Observable<void> {
        return this.httpClient.put<void>(`api/approval/${approvalId.value}`, {});
    }
}

export interface ApprovalOverviewDto {
    approvalId: ApprovalId;
    organizationId: OrganizationId;
    organizationName: string;
    author: string;
    date: string;
    sources: string;
}

export interface ApprovalDetailDto {
    approvalId: ApprovalId,
    organizationName: string,
    date: string,
    organization: Organization,
    proposedDomainEvent: ProposedChangeOrganizationEventDto
}

export interface ProposedChangeOrganizationEventDto {
    organizationId: OrganizationId;
    author: string;
    sources: string;
    changes: OrganizationEvent[];
}

export interface ApprovalId {
    value: string;
}
