import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from "@angular/common/http";

@Injectable({
    providedIn: 'root'
})
export class QuestionService {

    constructor(private httpClient: HttpClient) {
    }

    findQuestions(): Observable<Array<Question>> {
        return this.httpClient.get<Array<Question>>('api/questions');
    }

}

export class Question {
    public id: string;
    public question: string;
    public description: string;
    public position: number;
}