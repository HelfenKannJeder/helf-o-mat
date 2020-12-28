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

export interface ContactRequestResult {
    contactRequestId: ContactRequestId;
}

export interface ContactRequestId {
    value: string;
}