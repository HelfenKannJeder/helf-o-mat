package de.helfenkannjeder.helfomat.core.organization.event;

import de.helfenkannjeder.helfomat.core.organization.Organization;
import de.helfenkannjeder.helfomat.core.organization.OrganizationEventVisitor;
import de.helfenkannjeder.helfomat.core.organization.OrganizationId;
import de.helfenkannjeder.helfomat.core.organization.QuestionAnswer;

/**
 * @author Valentin Zickner
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
public class OrganizationEditDeleteQuestionAnswerEvent extends OrganizationEditEvent {
    private QuestionAnswer questionAnswer;

    protected OrganizationEditDeleteQuestionAnswerEvent() {
    }

    public OrganizationEditDeleteQuestionAnswerEvent(OrganizationId organizationId, QuestionAnswer questionAnswer) {
        super(organizationId);
        this.questionAnswer = questionAnswer;
    }

    public QuestionAnswer getQuestionAnswer() {
        return questionAnswer;
    }

    @Override
    public Organization.Builder applyOnOrganizationBuilder(Organization.Builder organization) {
        return organization.removeQuestionAnswer(questionAnswer);
    }

    @Override
    public <T> T visit(OrganizationEventVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return "OrganizationEditDeleteQuestionAnswerEvent{" +
            "questionAnswer=" + questionAnswer +
            '}';
    }
}
