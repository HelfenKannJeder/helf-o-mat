package de.helfenkannjeder.helfomat.core.organization.event

import de.helfenkannjeder.helfomat.core.organization.Organization
import de.helfenkannjeder.helfomat.core.organization.OrganizationEventVisitor
import de.helfenkannjeder.helfomat.core.organization.OrganizationId

/**
 * @author Valentin Zickner
 */
data class ConfirmedChangeOrganizationEvent(
    override val organizationId: OrganizationId,
    val approvedBy: String,
    val author: String,
    val sources: String,
    val changes: List<OrganizationEvent>
) : OrganizationEvent(organizationId) {

    override fun applyOnOrganizationBuilder(organizationBuilder: Organization.Builder?, strictMode: Boolean): Organization.Builder? {
        var result = organizationBuilder
        for (change in changes) {
            result = change.applyOnOrganizationBuilder(result, false)
        }
        return result
    }

    override fun <T> visit(visitor: OrganizationEventVisitor<T>): T = visitor.visit(this)

}