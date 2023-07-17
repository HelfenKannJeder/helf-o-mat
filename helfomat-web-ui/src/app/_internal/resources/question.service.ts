import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from 'rxjs';
import {HttpClient} from "@angular/common/http";
import questions from "./questions.json";

@Injectable({
    providedIn: 'root'
})
export class QuestionService {

    constructor(private httpClient: HttpClient) {
    }

    findQuestions(): Observable<Array<Question>> {
        return new BehaviorSubject<Array<Question>>(questions).asObservable();
    }

}

export class Question {
    public id: string;
    public question: string;
    public description: string;
    public position?: number;
}