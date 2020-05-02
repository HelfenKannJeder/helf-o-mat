package de.helfenkannjeder.helfomat.api.organization.event

import de.helfenkannjeder.helfomat.api.question.AnsweredQuestionDto
import de.helfenkannjeder.helfomat.core.organization.OrganizationId

/**
 * @author Valentin Zickner
 */
data class OrganizationEditDeleteQuestionAnswerEventDto(
    override val organizationId: OrganizationId,
    val answeredQuestion: AnsweredQuestionDto
) : OrganizationEventDto {

    override fun <T> visit(visitor: OrganizationEventDtoVisitor<T>): T {
        return visitor.visit(this)
    }

}