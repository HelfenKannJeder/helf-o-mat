package de.helfenkannjeder.helfomat.api.organization.event

import de.helfenkannjeder.helfomat.core.organization.OrganizationId

/**
 * @author Valentin Zickner
 */
data class OrganizationDeleteEventDto(
    override val organizationId: OrganizationId,
    override val eventApplicable: Boolean
) : OrganizationEventDto {

    override fun <T> visit(visitor: OrganizationEventDtoVisitor<T>): T {
        return visitor.visit(this)
    }

}