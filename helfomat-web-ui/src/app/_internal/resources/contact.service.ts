import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {OrganizationId} from "./organization.service";

@Injectable({
    providedIn: 'root'
})
export class ContactService {

    constructor(
        private httpClient: HttpClient
    ) {
    }

    public createContactRequest(createContactRequest: CreateContactRequest): Observable<ContactRequestResult> {
        return this.httpClient.post<ContactRequestResult>('api/contact/request', createContactRequest);
    }

    public resendContactRequest(resendContactRequest: ResendContactRequestDto): Observable<ContactRequestResult> {
        return this.httpClient.post<ContactRequestResult>('api/contact/resend', resendContactRequest);
    }

    public confirmContactRequest(confirmContactRequest: ConfirmContactRequest): Observable<ConfirmContactRequestResult> {
        return this.httpClient.post<ConfirmContactRequestResult>('api/contact/confirm', confirmContactRequest);
    }

    public getContactRequest(contactRequestId: ContactRequestId): Observable<ConfirmContactRequestResult> {
        return this.httpClient.get<ConfirmContactRequestResult>(`api/contact/${contactRequestId.value}`);
    }

}


export interface CreateContactRequest {
    captcha: string;
    name: string;
    email: string;
    subject: string;
    message: string;
    organizationId: OrganizationId;
    organizationContactPersonIndex: number;
}

export interface ResendContactRequestDto {
    contactRequestId: ContactRequestId;
    captcha: string;
}

export interface ConfirmContactRequest {
    contactRequestId: ContactRequestId;
    confirmationCode: string;
}

export interface ContactRequestResult {
    contactRequestId: ContactRequestId;
}

export interface ConfirmContactRequestResult {
    organizationId: OrganizationId;
    organizationUrl: string;
}

export interface ContactRequestId {
    value: string;
}