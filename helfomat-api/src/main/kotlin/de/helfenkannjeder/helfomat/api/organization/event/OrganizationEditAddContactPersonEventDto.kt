package de.helfenkannjeder.helfomat.api.organization.event

import de.helfenkannjeder.helfomat.api.organization.ContactPersonDto
import de.helfenkannjeder.helfomat.core.organization.OrganizationId

/**
 * @author Valentin Zickner
 */
data class OrganizationEditAddContactPersonEventDto(
    override val organizationId: OrganizationId,
    val index: Int,
    val contactPerson: ContactPersonDto,
    override val eventApplicable: Boolean
) : OrganizationEventDto {

    override fun <T> visit(visitor: OrganizationEventDtoVisitor<T>): T {
        return visitor.visit(this)
    }

}