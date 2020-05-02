package de.helfenkannjeder.helfomat.core.organization.event

import de.helfenkannjeder.helfomat.core.organization.Address
import de.helfenkannjeder.helfomat.core.organization.Organization
import de.helfenkannjeder.helfomat.core.organization.OrganizationEventVisitor
import de.helfenkannjeder.helfomat.core.organization.OrganizationId

/**
 * @author Valentin Zickner
 */
data class OrganizationEditDeleteAddressEvent(
    override val organizationId: OrganizationId,
    val address: Address
) : OrganizationEditEvent(organizationId) {

    override fun applyOnOrganizationBuilder(organizationBuilder: Organization.Builder?): Organization.Builder? {
        organizationBuilder?.addresses?.remove(address)
        return organizationBuilder
    }

    override fun <T> visit(visitor: OrganizationEventVisitor<T>): T {
        return visitor.visit(this)
    }

}