package de.helfenkannjeder.helfomat.core.organization.event

import de.helfenkannjeder.helfomat.core.organization.Organization
import de.helfenkannjeder.helfomat.core.organization.OrganizationEventVisitor
import de.helfenkannjeder.helfomat.core.organization.OrganizationId
import de.helfenkannjeder.helfomat.core.organization.OrganizationType

/**
 * @author Valentin Zickner
 */
data class OrganizationCreateEvent(
    override val organizationId: OrganizationId,
    val name: String,
    val urlName: String,
    val organizationType: OrganizationType
) : OrganizationEvent(organizationId) {

    override fun applyOnOrganizationBuilder(organizationBuilder: Organization.Builder?) =
        organizationBuilder
            ?.setId(organizationId)
            ?.setName(name)
            ?.setUrlName(urlName)
            ?.setOrganizationType(organizationType)
            ?: Organization.Builder(
                id = organizationId,
                name = name,
                urlName = urlName,
                organizationType = organizationType
            )

    override fun <T> visit(visitor: OrganizationEventVisitor<T>): T = visitor.visit(this)

}