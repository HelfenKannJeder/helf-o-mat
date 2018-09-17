import {Injectable} from '@angular/core';
import {Question} from './question.model';
import {Observable} from 'rxjs';
import {HttpClient} from "@angular/common/http";

@Injectable()
export class HelfomatService {

    constructor(private httpClient: HttpClient) {
    }

    findQuestions(): Observable<Array<Question>> {
        return this.httpClient.get<Array<Question>>('api/questions');
    }

}
