import {Injectable} from "@angular/core";
import {Http, Response} from "@angular/http";
import "rxjs/add/operator/map";
import {Observable, Subject} from "rxjs";
import Organisation from "../organisation/organisation.model";

@Injectable()
export class SearchService {

    private _organisations$: Subject<Organisation[]>;
    private dataStore: {
        organisations: Organisation[]
    };

    constructor(private http: Http) {
        this._organisations$ = <Subject<Organisation[]>>new Subject();
        this.dataStore = {
            organisations: []
        };
    }

    searchOrganisations(): Observable<Array<Organisation>> {
        return this.http.post('api/search', {}).map((r: Response) => r.json());
    }

    get organisations$() {
        return this._organisations$.asObservable();
    }

    search(answers) {
        this.http.post('api/search', answers).map((response: Response) => response.json()).subscribe(data => {
            this.dataStore.organisations = data;
            this._organisations$.next(this.dataStore.organisations);
        })
    }

}
