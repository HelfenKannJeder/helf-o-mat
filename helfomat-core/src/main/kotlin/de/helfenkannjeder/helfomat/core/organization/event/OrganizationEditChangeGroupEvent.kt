package de.helfenkannjeder.helfomat.core.organization.event

import de.helfenkannjeder.helfomat.core.organization.Group
import de.helfenkannjeder.helfomat.core.organization.Organization
import de.helfenkannjeder.helfomat.core.organization.OrganizationEventVisitor
import de.helfenkannjeder.helfomat.core.organization.OrganizationId
import java.util.function.BiFunction


/**
 * @author Valentin Zickner
 */
data class OrganizationEditChangeGroupEvent(
    override val organizationId: OrganizationId,
    val indexOffset: Int,
    val oldGroup: Group,
    val group: Group
) : OrganizationEditEvent(organizationId) {

    override fun applyOnOrganizationBuilder(organizationBuilder: Organization.Builder?, strictMode: Boolean): Organization.Builder? {
        val groups = organizationBuilder?.groups ?: return organizationBuilder
        organizationBuilder.groups = changePosition(
            groups,
            oldGroup,
            group,
            indexOffset,
            when (strictMode) {
                true -> null
                false -> ClosestMatchFinder()
            }
        )
        return organizationBuilder
    }

    override fun <T> visit(visitor: OrganizationEventVisitor<T>): T {
        return visitor.visit(this)
    }

    class ClosestMatchFinder : BiFunction<MutableList<Group>, Group, Int> {
        override fun apply(groups: MutableList<Group>, group: Group): Int {
            val potentialGroups = groups.filter { it.name == group.name }
            if (potentialGroups.size == 1) {
                return groups.indexOf(potentialGroups.first())
            }
            return -1
        }

    }

}