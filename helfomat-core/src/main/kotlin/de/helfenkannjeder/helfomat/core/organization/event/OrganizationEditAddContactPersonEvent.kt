package de.helfenkannjeder.helfomat.core.organization.event

import de.helfenkannjeder.helfomat.core.organization.ContactPerson
import de.helfenkannjeder.helfomat.core.organization.Organization
import de.helfenkannjeder.helfomat.core.organization.OrganizationEventVisitor
import de.helfenkannjeder.helfomat.core.organization.OrganizationId

/**
 * @author Valentin Zickner
 */
data class OrganizationEditAddContactPersonEvent(
    override val organizationId: OrganizationId,
    val index: Int,
    val contactPerson: ContactPerson
) : OrganizationEditEvent(organizationId) {

    override fun applyOnOrganizationBuilder(organizationBuilder: Organization.Builder?, strictMode: Boolean): Organization.Builder? {
        if (organizationBuilder?.contactPersons != null) {
            if (organizationBuilder.contactPersons.size > index) {
                organizationBuilder.contactPersons.add(index, contactPerson)
            } else {
                organizationBuilder.contactPersons.add(contactPerson)
            }
        }
        return organizationBuilder
    }

    override fun <T> visit(visitor: OrganizationEventVisitor<T>): T {
        return visitor.visit(this)
    }

}