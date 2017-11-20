package de.helfenkannjeder.helfomat.api.question;

import de.helfenkannjeder.helfomat.core.question.Answer;

/**
 * @author Valentin Zickner
 */
public class AnsweredQuestionDto extends QuestionDto {
    private Answer answer = Answer.MAYBE;

    public AnsweredQuestionDto(String question, String description, Answer answer, int position) {
        super(question, description, position);
        this.answer = answer;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

}
