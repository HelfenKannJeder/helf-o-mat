package de.helfenkannjeder.helfomat.api.question;

import de.helfenkannjeder.helfomat.core.question.Answer;

/**
 * @author Valentin Zickner
 */
public class QuestionAnswerDto {
    private String id;
    private Answer answer;

    public String getId() {
        return id;
    }

    public Answer getAnswer() {
        return answer;
    }
}
