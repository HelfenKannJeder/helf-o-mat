package de.helfenkannjeder.helfomat.api.organization.event

import de.helfenkannjeder.helfomat.core.organization.OrganizationId
import de.helfenkannjeder.helfomat.core.organization.OrganizationType

/**
 * @author Valentin Zickner
 */
data class OrganizationCreateEventDto(
    override val organizationId: OrganizationId,
    val name: String,
    val urlName: String,
    val organizationType: OrganizationType,
    override val eventApplicable: EventApplicability
) : OrganizationEventDto {

    override fun <T> visit(visitor: OrganizationEventDtoVisitor<T>): T {
        return visitor.visit(this)
    }

}