package de.helfenkannjeder.helfomat.core.organization.event

import de.helfenkannjeder.helfomat.core.organization.ContactPerson
import de.helfenkannjeder.helfomat.core.organization.Organization
import de.helfenkannjeder.helfomat.core.organization.OrganizationEventVisitor
import de.helfenkannjeder.helfomat.core.organization.OrganizationId

/**
 * @author Valentin Zickner
 */
data class OrganizationEditChangeContactPersonEvent(
    override val organizationId: OrganizationId,
    val indexOffset: Int,
    val oldContactPerson: ContactPerson,
    val contactPerson: ContactPerson
) : OrganizationEditEvent(organizationId) {

    override fun applyOnOrganizationBuilder(organizationBuilder: Organization.Builder?): Organization.Builder? {
        val contactPersons = organizationBuilder?.contactPersons ?: return organizationBuilder
        organizationBuilder.contactPersons = changePosition(contactPersons, oldContactPerson, contactPerson, indexOffset)
        return organizationBuilder
    }

    override fun <T> visit(visitor: OrganizationEventVisitor<T>): T {
        return visitor.visit(this)
    }

}