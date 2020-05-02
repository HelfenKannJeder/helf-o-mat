package de.helfenkannjeder.helfomat.core.organization.event

import de.helfenkannjeder.helfomat.core.organization.Organization
import de.helfenkannjeder.helfomat.core.organization.OrganizationEventVisitor
import de.helfenkannjeder.helfomat.core.organization.OrganizationId
import de.helfenkannjeder.helfomat.core.organization.Volunteer

/**
 * @author Valentin Zickner
 */
data class OrganizationEditAddVolunteerEvent(
    override val organizationId: OrganizationId,
    val index: Int,
    val volunteer: Volunteer
) : OrganizationEditEvent(organizationId) {

    override fun applyOnOrganizationBuilder(organizationBuilder: Organization.Builder?): Organization.Builder? {
        organizationBuilder?.volunteers?.add(index, volunteer)
        return organizationBuilder
    }

    override fun <T> visit(visitor: OrganizationEventVisitor<T>): T {
        return visitor.visit(this)
    }

}