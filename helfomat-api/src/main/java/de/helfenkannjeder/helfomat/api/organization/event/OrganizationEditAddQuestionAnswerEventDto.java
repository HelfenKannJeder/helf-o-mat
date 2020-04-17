package de.helfenkannjeder.helfomat.api.organization.event;

import de.helfenkannjeder.helfomat.api.question.AnsweredQuestionDto;
import de.helfenkannjeder.helfomat.core.organization.OrganizationId;

/**
 * @author Valentin Zickner
 */
public class OrganizationEditAddQuestionAnswerEventDto extends OrganizationEventDto {
    private int index;
    private AnsweredQuestionDto answeredQuestion;

    OrganizationEditAddQuestionAnswerEventDto() {
    }

    public OrganizationEditAddQuestionAnswerEventDto(OrganizationId organizationId, int index, AnsweredQuestionDto answeredQuestion) {
        super(organizationId);
        this.index = index;
        this.answeredQuestion = answeredQuestion;
    }

    public int getIndex() {
        return index;
    }

    public AnsweredQuestionDto getAnsweredQuestion() {
        return answeredQuestion;
    }

    @Override
    public <T> T visit(OrganizationEventDtoVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
