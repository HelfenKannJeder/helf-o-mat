package de.helfenkannjeder.helfomat.core.organisation.event;

import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;
import de.helfenkannjeder.helfomat.core.organisation.OrganizationEventVisitor;
import de.helfenkannjeder.helfomat.core.organisation.QuestionAnswer;

/**
 * @author Valentin Zickner
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
public class OrganisationEditAddQuestionAnswerEvent extends OrganisationEditEvent {
    private int index;
    private QuestionAnswer questionAnswer;

    protected OrganisationEditAddQuestionAnswerEvent() {
    }

    public OrganisationEditAddQuestionAnswerEvent(OrganisationId organisationId, int index, QuestionAnswer questionAnswer) {
        super(organisationId);
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
    public Organisation.Builder applyOnOrganisationBuilder(Organisation.Builder organisation) {
        return organisation.addQuestionAnswer(index, questionAnswer);
    }

    @Override
    public <T> T visit(OrganizationEventVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
