package de.helfenkannjeder.helfomat.core.organization.event

import de.helfenkannjeder.helfomat.core.organization.Organization
import de.helfenkannjeder.helfomat.core.organization.OrganizationEventVisitor
import de.helfenkannjeder.helfomat.core.organization.OrganizationId
import de.helfenkannjeder.helfomat.core.picture.PictureId

/**
 * @author Valentin Zickner
 */
data class OrganizationEditChangePictureEvent(
    override val organizationId: OrganizationId,
    val indexOffset: Int,
    val pictureId: PictureId
) : OrganizationEditEvent(organizationId) {

    override fun applyOnOrganizationBuilder(organizationBuilder: Organization.Builder?): Organization.Builder? {
        val pictures = organizationBuilder?.pictures ?: return organizationBuilder
        organizationBuilder.pictures = changePosition(pictures, pictureId, pictureId, indexOffset)
        return organizationBuilder
    }

    override fun <T> visit(visitor: OrganizationEventVisitor<T>): T {
        return visitor.visit(this)
    }

}