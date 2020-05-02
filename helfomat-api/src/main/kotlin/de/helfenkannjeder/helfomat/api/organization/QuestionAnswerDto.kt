package de.helfenkannjeder.helfomat.api.organization;

import de.helfenkannjeder.helfomat.core.organization.Answer;

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
