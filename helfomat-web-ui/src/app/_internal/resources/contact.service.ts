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

    public createContactRequest(createContactRequest: CreateContactRequest): Observable<any> {
        return this.httpClient.post('api/contact/request', createContactRequest);
    }

}


export interface CreateContactRequest {
    captcha: string;
    email: string;
    subject: string;
    message: string;
    organizationId: OrganizationId;
    organizationContactPersonIndex: number;
}