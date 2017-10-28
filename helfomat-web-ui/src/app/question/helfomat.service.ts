import {Injectable} from '@angular/core';
import {Question} from './question.model';
import {Http, Response} from '@angular/http';
import 'rxjs/add/operator/map';
import {Observable} from 'rxjs';

@Injectable()
export class HelfomatService {

    constructor(private http: Http) {
    }

    findQuestions(): Observable<Array<Question>> {
        return this.http.get('api/questions').map((r: Response) => r.json());
    }

}
