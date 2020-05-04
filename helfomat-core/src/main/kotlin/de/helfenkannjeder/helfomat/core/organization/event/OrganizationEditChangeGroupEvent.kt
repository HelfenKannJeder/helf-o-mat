package de.helfenkannjeder.helfomat.core.organization.event

import de.helfenkannjeder.helfomat.core.organization.Group
import de.helfenkannjeder.helfomat.core.organization.Organization
import de.helfenkannjeder.helfomat.core.organization.OrganizationEventVisitor
import de.helfenkannjeder.helfomat.core.organization.OrganizationId


/**
 * @author Valentin Zickner
 */
data class OrganizationEditChangeGroupEvent(
    override val organizationId: OrganizationId,
    val indexOffset: Int,
    val oldGroup: Group,
    override val group: Group
) : OrganizationEditGroupEvent(organizationId, group) {

    override fun applyOnOrganizationBuilder(organizationBuilder: Organization.Builder?): Organization.Builder? {
        val groups = organizationBuilder?.groups ?: return organizationBuilder
        val indexOf = groups.indexOf(oldGroup);
        if (indexOf != -1) {
            groups.removeAt(indexOf)
            groups.add(indexOf + indexOffset, group)
        }
        return organizationBuilder
    }

    override fun <T> visit(visitor: OrganizationEventVisitor<T>): T {
        return visitor.visit(this)
    }

}