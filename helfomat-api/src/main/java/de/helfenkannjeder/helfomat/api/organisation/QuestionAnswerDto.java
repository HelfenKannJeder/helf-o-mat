package de.helfenkannjeder.helfomat.api.organisation;

import de.helfenkannjeder.helfomat.core.organisation.Answer;

/**
 * @author Valentin Zickner
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal", "unused"})
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
