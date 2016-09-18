/* tslint:disable:no-unused-variable */

import {TestBed, inject, tick} from '@angular/core/testing';
import {HelfomatService} from './helfomat.service';
import {MockBackend, MockConnection} from '@angular/http/testing';
import {Response, ResponseOptions, Http, ConnectionBackend, BaseRequestOptions, RequestOptions} from '@angular/http';

describe('Component: Question', () => {

    describe('Service: Helfomat', () => {
        beforeEach(() => {
            TestBed.configureTestingModule({
                providers: [
                    {provide: RequestOptions, useClass: BaseRequestOptions},
                    {provide: ConnectionBackend, useClass: MockBackend},
                    Http,
                    HelfomatService
                ]
            });
        });

        it('should inject the service', inject([HelfomatService], (service: HelfomatService) => {
            expect(service).toBeTruthy();
        }));

        it('should download all questions if requested', inject([ConnectionBackend, HelfomatService],
            (backend: MockBackend,
             service: HelfomatService) => {
                // Arrange
                let questions = null;
                backend.connections.subscribe((c: MockConnection) => {
                    expect(c.request.url).toEqual('api/questions');
                    c.mockRespond(new Response(new ResponseOptions({body: '[{"question":"Test"}]'})));
                });

                // Act
                service.findQuestions().subscribe((q) => {
                    questions = q;
                });

                // Assert
                backend.verifyNoPendingRequests();
                expect(questions).toEqual([{question:'Test'}]);
            }));
    });
});
