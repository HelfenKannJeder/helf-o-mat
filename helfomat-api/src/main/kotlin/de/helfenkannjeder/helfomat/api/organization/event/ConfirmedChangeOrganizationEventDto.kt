package de.helfenkannjeder.helfomat.api.organization.event

import de.helfenkannjeder.helfomat.core.organization.OrganizationId

/**
 * @author Valentin Zickner
 */
data class ConfirmedChangeOrganizationEventDto(
    override val organizationId: OrganizationId,
    val approvedBy: String,
    val author: String,
    val sources: String,
    val changes: List<OrganizationEventDto>,
    override val eventApplicable: Boolean
) : OrganizationEventDto {

    override fun <T> visit(visitor: OrganizationEventDtoVisitor<T>): T {
        return visitor.visit(this)
    }

}