import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class ContactService {

    constructor(
        private httpClient: HttpClient
    ) {
    }

    public createContactRequest(createContactRequest: CreateContactRequest): Observable<void> {
        return this.httpClient.post('api/contact/request', createContactRequest);
    }

}


export interface CreateContactRequest {
    email: string;
    subject: string;
    message: string;
}