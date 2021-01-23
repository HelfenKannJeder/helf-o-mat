package de.helfenkannjeder.helfomat.api.organization.event

import de.helfenkannjeder.helfomat.api.organization.AddressDto
import de.helfenkannjeder.helfomat.core.organization.OrganizationId

/**
 * @author Valentin Zickner
 */
data class OrganizationEditDeleteAddressEventDto(
    override val organizationId: OrganizationId,
    val address: AddressDto,
    override val eventApplicable: Boolean
) : OrganizationEventDto {

    override fun <T> visit(visitor: OrganizationEventDtoVisitor<T>): T {
        return visitor.visit(this)
    }

}