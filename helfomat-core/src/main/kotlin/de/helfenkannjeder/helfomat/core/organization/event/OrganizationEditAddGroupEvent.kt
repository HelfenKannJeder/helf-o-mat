package de.helfenkannjeder.helfomat.core.organization.event

import de.helfenkannjeder.helfomat.core.organization.Group
import de.helfenkannjeder.helfomat.core.organization.Organization
import de.helfenkannjeder.helfomat.core.organization.OrganizationEventVisitor
import de.helfenkannjeder.helfomat.core.organization.OrganizationId

/**
 * @author Valentin Zickner
 */
data class OrganizationEditAddGroupEvent(
    override val organizationId: OrganizationId,
    val index: Int,
    override val group: Group
) : OrganizationEditGroupEvent(organizationId, group) {

    override fun applyOnOrganizationBuilder(organizationBuilder: Organization.Builder?): Organization.Builder? {
        organizationBuilder?.groups?.add(index, group)
        return organizationBuilder
    }

    override fun <T> visit(visitor: OrganizationEventVisitor<T>): T {
        return visitor.visit(this)
    }

}