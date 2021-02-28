package de.helfenkannjeder.helfomat.api.organization.event

import de.helfenkannjeder.helfomat.api.organization.GroupDto
import de.helfenkannjeder.helfomat.core.organization.OrganizationId

data class OrganizationEditChangeGroupEventDto(
    override val organizationId: OrganizationId,
    val indexOffset: Int,
    val oldGroup: GroupDto,
    val group: GroupDto,
    override val eventApplicable: EventApplicability
) : OrganizationEventDto {

    override fun <T> visit(visitor: OrganizationEventDtoVisitor<T>): T = visitor.visit(this)

}
