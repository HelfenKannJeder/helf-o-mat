package de.helfenkannjeder.helfomat.api.organization.event;

import de.helfenkannjeder.helfomat.api.question.AnsweredQuestionDto;
import de.helfenkannjeder.helfomat.core.organization.OrganizationId;

/**
 * @author Valentin Zickner
 */
public class OrganizationEditDeleteQuestionAnswerEventDto extends OrganizationEventDto {
    private AnsweredQuestionDto answeredQuestion;

    OrganizationEditDeleteQuestionAnswerEventDto() {
    }

    public OrganizationEditDeleteQuestionAnswerEventDto(OrganizationId organizationId, AnsweredQuestionDto answeredQuestion) {
        super(organizationId);
        this.answeredQuestion = answeredQuestion;
    }

    public AnsweredQuestionDto getAnsweredQuestion() {
        return answeredQuestion;
    }

    @Override
    public <T> T visit(OrganizationEventDtoVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
