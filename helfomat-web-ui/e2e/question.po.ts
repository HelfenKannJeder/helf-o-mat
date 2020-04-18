import {browser, by, element} from 'protractor';

export class QuestionPage {

    navigateTo() {
        return browser.get('/');
    }

    currentUrl() {
        return browser.getCurrentUrl();
    }

    getHelfOMatQuestion() {
        return element
            .all((by.css('app-question h1')))
            .filter(QuestionPage.visible)
            .getText();
    }

    answerQuestionWithYes() {
        element
            .all(by.css('app-question .btn-yes'))
            .filter(QuestionPage.visible)
            .click();
    }

    answerQuestionWithMaybe() {
        element
            .all(by.css('app-question .btn-maybe'))
            .filter(QuestionPage.visible)
            .click();
    }

    answerQuestionWithNo() {
        element
            .all(by.css('app-question .btn-no'))
            .filter(QuestionPage.visible)
            .click();
    }

    answerQuestions(amountOfQuestions: number) {
        for (let i = 0; i < amountOfQuestions; i++) {
            switch (i % 3) {
                case 0:
                    this.answerQuestionWithYes();
                    break;
                case 1:
                    this.answerQuestionWithMaybe();
                    break;
                case 2:
                    this.answerQuestionWithNo();
                    break;
            }
        }
    }

    getNumberOfQuestions() {
        return element
            .all(by.css('app-question .question-progress .material-icons'))
            .filter(QuestionPage.visible)
            .count();
    }

    private static visible = (element) => element.isDisplayed();
}
