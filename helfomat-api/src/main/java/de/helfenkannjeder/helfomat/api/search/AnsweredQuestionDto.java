package de.helfenkannjeder.helfomat.api.search;

import de.helfenkannjeder.helfomat.core.organisation.Question;

/**
 * @author Valentin Zickner
 */
public class AnsweredQuestionDto extends QuestionDto {
    private AnswerDto answer = AnswerDto.MAYBE;

    public AnsweredQuestionDto() {
    }

    private AnsweredQuestionDto(String question, String description, AnswerDto answer, int position) {
        super(question, description, position);
        this.answer = answer;
    }

    public AnswerDto getAnswer() {
        return answer;
    }

    public void setAnswer(AnswerDto answer) {
        this.answer = answer;
    }

    static AnsweredQuestionDto fromQuestion(Question question) {
        return new AnsweredQuestionDto(
                question.getQuestion(),
                question.getDescription(),
                AnswerDto.fromAnswer(question.getAnswer()),
                question.getPosition()
        );
    }
}
