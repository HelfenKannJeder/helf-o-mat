package de.helfenkannjeder.helfomat.core.organization.event

import de.helfenkannjeder.helfomat.core.organization.Organization
import de.helfenkannjeder.helfomat.core.organization.OrganizationEventVisitor
import de.helfenkannjeder.helfomat.core.organization.OrganizationId
import de.helfenkannjeder.helfomat.core.organization.QuestionAnswer

/**
 * @author Valentin Zickner
 */
data class OrganizationEditChangeQuestionAnswerEvent(
    override val organizationId: OrganizationId,
    val indexOffset: Int,
    val oldQuestionAnswer: QuestionAnswer,
    val questionAnswer: QuestionAnswer
) : OrganizationEditEvent(organizationId) {

    override fun applyOnOrganizationBuilder(organizationBuilder: Organization.Builder?, strictMode: Boolean): Organization.Builder? {
        val questionAnswers = organizationBuilder?.questionAnswers ?: return organizationBuilder
        organizationBuilder.questionAnswers = changePosition(questionAnswers, oldQuestionAnswer, questionAnswer, indexOffset)
        return organizationBuilder
    }

    override fun <T> visit(visitor: OrganizationEventVisitor<T>): T {
        return visitor.visit(this)
    }

}