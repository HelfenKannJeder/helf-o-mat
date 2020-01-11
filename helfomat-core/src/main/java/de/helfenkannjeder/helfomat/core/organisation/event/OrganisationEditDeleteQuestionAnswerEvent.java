package de.helfenkannjeder.helfomat.core.organisation.event;

import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;
import de.helfenkannjeder.helfomat.core.organisation.QuestionAnswer;

/**
 * @author Valentin Zickner
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
public class OrganisationEditDeleteQuestionAnswerEvent extends OrganisationEditEvent {
    private QuestionAnswer questionAnswer;

    protected OrganisationEditDeleteQuestionAnswerEvent() {
    }

    public OrganisationEditDeleteQuestionAnswerEvent(OrganisationId organisationId, QuestionAnswer questionAnswer) {
        super(organisationId);
        this.questionAnswer = questionAnswer;
    }

    public QuestionAnswer getQuestionAnswer() {
        return questionAnswer;
    }

    @Override
    public Organisation.Builder applyOnOrganisationBuilder(Organisation.Builder organisation) {
        return organisation.removeQuestionAnswer(questionAnswer);
    }
}
