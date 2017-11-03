package de.helfenkannjeder.helfomat.api.question;

import de.helfenkannjeder.helfomat.core.question.Answer;
import de.helfenkannjeder.helfomat.core.question.Question;

/**
 * @author Valentin Zickner
 */
public class AnsweredQuestionDto extends QuestionDto {
    private Answer answer = Answer.MAYBE;

    public AnsweredQuestionDto() {
    }

    private AnsweredQuestionDto(String question, String description, Answer answer, int position) {
        super(question, description, position);
        this.answer = answer;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public static AnsweredQuestionDto fromQuestion(Question question) {
        return new AnsweredQuestionDto(
                question.getQuestion(),
                question.getDescription(),
                question.getAnswer(),
                question.getPosition()
        );
    }
}
