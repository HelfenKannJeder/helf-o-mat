package de.helfenkannjeder.helfomat.api.organization.event

import de.helfenkannjeder.helfomat.api.organization.AddressDto
import de.helfenkannjeder.helfomat.core.organization.OrganizationId

data class OrganizationEditChangeAddressEventDto(
    override val organizationId: OrganizationId,
    val indexOffset: Int,
    val oldAddress: AddressDto,
    val address: AddressDto,
    override val eventApplicable: Boolean
) : OrganizationEventDto {

    override fun <T> visit(visitor: OrganizationEventDtoVisitor<T>): T = visitor.visit(this)

}
