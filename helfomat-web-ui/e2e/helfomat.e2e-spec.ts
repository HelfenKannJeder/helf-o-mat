import {QuestionPage} from './question.po';
import {ResultPage} from './result.po';
import {LocationPage} from './location.po';

let NUMBER_OF_HELFOMAT_QUESTIONS = 18;

describe('Helf-O-Mat', function () {
    let questionPage: QuestionPage;
    let locationPage: LocationPage;
    let resultPage: ResultPage;

    beforeEach(() => {
        questionPage = new QuestionPage();
        locationPage = new LocationPage();
        resultPage = new ResultPage();
    });

    it('should display initially the first question', () => {
        questionPage.navigateTo();
        expect(questionPage.getHelfOMatQuestion()).toMatch('Möchtest Du gerne Einsatzfahrzeuge - ggf. auch mit Blaulicht und Martinshorn - fahren?');
    });

    it('should go to the next question if YES button is pressed', () => {
        questionPage.navigateTo();
        questionPage.answerQuestionWithYes();
        expect(questionPage.getHelfOMatQuestion()).toMatch('Möchtest Du Dich in medizinischer Hilfe ausbilden lassen und diese leisten?');
        expect(questionPage.currentUrl()).toMatch(new RegExp('/question.*'));
    });

    it('should contain all questions in progress bar', () => {
        questionPage.navigateTo();
        expect(questionPage.getNumberOfQuestions()).toEqual(NUMBER_OF_HELFOMAT_QUESTIONS);
    });

    it('should stay on question page before answering all questions', () => {
        questionPage.navigateTo();
        questionPage.answerQuestions(NUMBER_OF_HELFOMAT_QUESTIONS - 1);
        expect(questionPage.currentUrl()).toMatch(new RegExp('/question.*'));
    });

    it('should redirect to location page after answering all questions', () => {
        questionPage.navigateTo();
        questionPage.answerQuestions(NUMBER_OF_HELFOMAT_QUESTIONS);
        expect(questionPage.currentUrl()).toMatch(new RegExp('/location.*'));
        expect(locationPage.hasMap()).toEqual(true);
    });
});
