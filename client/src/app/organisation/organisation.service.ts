import {Injectable} from '@angular/core';
import {Organisation} from './organisation.model';
import {Observable} from 'rxjs';
import {Http, Response} from '@angular/http';

@Injectable()
export class OrganisationService {

    constructor(private http: Http) {
    }


    getOrganisation(id: string): Observable<Organisation> {
        return this.http.get('api/organisation/' + id)
            .map((response: Response) => response.json());
    }

}