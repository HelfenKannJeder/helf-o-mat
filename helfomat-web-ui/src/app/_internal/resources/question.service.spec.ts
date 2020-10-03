/* tslint:disable:no-unused-variable */

import {inject, TestBed} from '@angular/core/testing';
import {Question, QuestionService} from './question.service';
import {MockBackend, MockConnection} from '@angular/http/testing';
import {BaseRequestOptions, ConnectionBackend, Http, RequestOptions, Response, ResponseOptions} from '@angular/http';

describe('Component: Question', () => {

    describe('Service: Helfomat', () => {
        beforeEach(() => {
            TestBed.configureTestingModule({
                providers: [
                    {provide: RequestOptions, useClass: BaseRequestOptions},
                    {provide: ConnectionBackend, useClass: MockBackend},
                    Http,
                    QuestionService
                ]
            });
        });

        it('should inject the service', inject([QuestionService], (service: QuestionService) => {
            expect(service).toBeTruthy();
        }));

        it('should download all questions if requested', inject([ConnectionBackend, QuestionService],
            (backend: MockBackend,
             service: QuestionService) => {
                // Arrange
                let questions = null;
                backend.connections.subscribe((c: MockConnection) => {
                    expect(c.request.url).toEqual('api/questions');
                    c.mockRespond(new Response(new ResponseOptions({body: '[{"question":"Test"}]'})));
                });

                // Act
                service.findQuestions().subscribe((q: Question[]) => {
                    questions = q;
                });

                // Assert
                backend.verifyNoPendingRequests();
                expect(questions).toEqual([{question:'Test'}]);
            }));
    });
});
