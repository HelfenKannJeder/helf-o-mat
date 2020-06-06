package de.helfenkannjeder.helfomat.api.organization.event

import de.helfenkannjeder.helfomat.api.organization.ContactPersonDto
import de.helfenkannjeder.helfomat.core.organization.OrganizationId

data class OrganizationEditChangeContactPersonEventDto(
    override val organizationId: OrganizationId,
    val indexOffset: Int,
    val oldContactPerson: ContactPersonDto,
    val contactPerson: ContactPersonDto
) : OrganizationEventDto {

    override fun <T> visit(visitor: OrganizationEventDtoVisitor<T>): T = visitor.visit(this)

}