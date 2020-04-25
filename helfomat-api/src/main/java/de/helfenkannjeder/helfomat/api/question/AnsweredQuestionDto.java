package de.helfenkannjeder.helfomat.api.question;

import de.helfenkannjeder.helfomat.core.organization.Answer;
import de.helfenkannjeder.helfomat.core.question.QuestionId;

/**
 * @author Valentin Zickner
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal", "unused"})
public class AnsweredQuestionDto {
    private QuestionId questionId;
    private String question;
    private Answer answer;

    private AnsweredQuestionDto() {
    }

    public AnsweredQuestionDto(QuestionId questionId, String question, Answer answer) {
        this.questionId = questionId;
        this.question = question;
        this.answer = answer;
    }

    public QuestionId getQuestionId() {
        return questionId;
    }

    public void setQuestionId(QuestionId questionId) {
        this.questionId = questionId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

}
