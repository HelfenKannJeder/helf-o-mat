package de.helfenkannjeder.helfomat.core.organisation;

import de.helfenkannjeder.helfomat.core.question.QuestionId;

/**
 * @author Valentin Zickner
 */
public class QuestionAnswer {
    private QuestionId questionId;
    private Answer answer;

    public QuestionAnswer() {
    }

    public QuestionAnswer(QuestionId questionId, Answer answer) {
        this.questionId = questionId;
        this.answer = answer;
    }

    public QuestionId getQuestionId() {
        return questionId;
    }

    public Answer getAnswer() {
        return answer;
    }

}
