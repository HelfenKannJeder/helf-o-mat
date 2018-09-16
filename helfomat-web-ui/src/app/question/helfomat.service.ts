import {Injectable} from '@angular/core';
import {Question} from './question.model';
import {Http, Response} from '@angular/http';
import {Observable} from 'rxjs';
import {map} from "rxjs/operators";

@Injectable()
export class HelfomatService {

    constructor(private http: Http) {
    }

    findQuestions(): Observable<Array<Question>> {
        return this.http.get('api/questions').pipe(map((r: Response) => r.json()));
    }

}
