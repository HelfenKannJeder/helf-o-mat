import {inject, TestBed} from '@angular/core/testing';
import {Question, QuestionService} from './question.service';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';

describe('Component: Question', () => {

    describe('Service: Helfomat', () => {

        let httpTestingController: HttpTestingController;
        let questionService: QuestionService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                providers: [
                    QuestionService
                ],
                imports: [
                    HttpClientTestingModule
                ]
            });

            httpTestingController = TestBed.inject(HttpTestingController);
            questionService = TestBed.inject(QuestionService);
        });

        it('should inject the service', inject([QuestionService], (service: QuestionService) => {
            expect(service).toBeTruthy();
        }));

        it('should download all questions if requested', () => {
            // Arrange
            const testRequest = httpTestingController.expectOne('api/questions');
            expect(testRequest.request.method).toEqual('GET');
            let questions = null;

            // Act
            questionService.findQuestions().subscribe((q: Question[]) => {
                questions = q;
            });

            // Assert
            httpTestingController.verify();
            expect(questions).toEqual([{question: 'Test'}]);
            testRequest.flush([{"question": "Test"}]);
        });
    });
});
