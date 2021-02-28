package de.helfenkannjeder.helfomat.core.organization.event

import de.helfenkannjeder.helfomat.core.organization.Organization
import de.helfenkannjeder.helfomat.core.organization.OrganizationEventVisitor
import de.helfenkannjeder.helfomat.core.organization.OrganizationId
import de.helfenkannjeder.helfomat.core.organization.QuestionAnswer

/**
 * @author Valentin Zickner
 */
data class OrganizationEditAddQuestionAnswerEvent(
    override val organizationId: OrganizationId,
    val index: Int,
    val questionAnswer: QuestionAnswer
) : OrganizationEditEvent(organizationId) {

    override fun applyOnOrganizationBuilder(organizationBuilder: Organization.Builder?, strictMode: Boolean): Organization.Builder? {
        if (organizationBuilder?.questionAnswers != null) {
            if (organizationBuilder.questionAnswers.size > index) {
                organizationBuilder.questionAnswers.add(index, questionAnswer)
            } else {
                organizationBuilder.questionAnswers.add(questionAnswer)
            }
        }
        return organizationBuilder
    }

    override fun <T> visit(visitor: OrganizationEventVisitor<T>): T {
        return visitor.visit(this)
    }

}