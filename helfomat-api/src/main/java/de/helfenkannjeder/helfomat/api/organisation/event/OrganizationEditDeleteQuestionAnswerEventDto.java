package de.helfenkannjeder.helfomat.api.organisation.event;

import de.helfenkannjeder.helfomat.api.question.AnsweredQuestionDto;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;

/**
 * @author Valentin Zickner
 */
public class OrganizationEditDeleteQuestionAnswerEventDto extends OrganizationEventDto {
    private AnsweredQuestionDto answeredQuestion;

    OrganizationEditDeleteQuestionAnswerEventDto() {
    }

    public OrganizationEditDeleteQuestionAnswerEventDto(OrganisationId organisationId, AnsweredQuestionDto answeredQuestion) {
        super(organisationId);
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
