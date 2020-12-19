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

    findHistory(): Observable<ApprovalOverviewDto[]> {
        return this.httpClient.get<ApprovalOverviewDto[]>('api/approval/history');
    }

    findDetails(approvalId: ApprovalId): Observable<ApprovalDetailDto> {
        return this.httpClient.get<ApprovalDetailDto>(`api/approval/${approvalId.value}`);
    }

    confirmApproval(approvalId: ApprovalId, confirmedChanges: OrganizationEvent[]): Observable<void> {
        return this.httpClient.put<void>(`api/approval/${approvalId.value}`, confirmedChanges);
    }
}

export interface ApprovalOverviewDto {
    approvalId: ApprovalId;
    organizationId: OrganizationId;
    organizationName: string;
    author: string;
    approvedBy: string;
    date: string;
    sources: string;
}

export interface ApprovalDetailDto {
    approvalId: ApprovalId;
    organizationName: string;
    date: string;
    organization: Organization;
    proposedDomainEvent: ProposedChangeOrganizationEventDto;
    author: ApprovalAuthorDto;
    approved: boolean
}

export interface ApprovalAuthorDto {
    username: string;
    email: string;
    firstName: string;
    lastName: string;
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
