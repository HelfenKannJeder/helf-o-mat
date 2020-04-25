package de.helfenkannjeder.helfomat.core.organization.event;

import de.helfenkannjeder.helfomat.core.organization.Organization;
import de.helfenkannjeder.helfomat.core.organization.OrganizationEventVisitor;
import de.helfenkannjeder.helfomat.core.organization.OrganizationId;
import de.helfenkannjeder.helfomat.core.organization.QuestionAnswer;

/**
 * @author Valentin Zickner
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
public class OrganizationEditAddQuestionAnswerEvent extends OrganizationEditEvent {
    private int index;
    private QuestionAnswer questionAnswer;

    protected OrganizationEditAddQuestionAnswerEvent() {
    }

    public OrganizationEditAddQuestionAnswerEvent(OrganizationId organizationId, int index, QuestionAnswer questionAnswer) {
        super(organizationId);
        this.index = index;
        this.questionAnswer = questionAnswer;
    }

    public int getIndex() {
        return index;
    }

    public QuestionAnswer getQuestionAnswer() {
        return questionAnswer;
    }

    @Override
    public Organization.Builder applyOnOrganizationBuilder(Organization.Builder organization) {
        return organization.addQuestionAnswer(index, questionAnswer);
    }

    @Override
    public <T> T visit(OrganizationEventVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return "OrganizationEditAddQuestionAnswerEvent{" +
            "index=" + index +
            ", questionAnswer=" + questionAnswer +
            '}';
    }
}
