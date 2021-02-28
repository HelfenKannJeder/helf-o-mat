package de.helfenkannjeder.helfomat.core.organization.event

import de.helfenkannjeder.helfomat.core.organization.Organization
import de.helfenkannjeder.helfomat.core.organization.OrganizationEventVisitor
import de.helfenkannjeder.helfomat.core.organization.OrganizationId
import de.helfenkannjeder.helfomat.core.picture.PictureId

/**
 * @author Valentin Zickner
 */
data class OrganizationEditAddPictureEvent(
    override val organizationId: OrganizationId,
    val index: Int,
    val pictureId: PictureId
) : OrganizationEditEvent(organizationId) {

    override fun applyOnOrganizationBuilder(organizationBuilder: Organization.Builder?, strictMode: Boolean): Organization.Builder? {
        if (organizationBuilder?.pictures != null) {
            if (organizationBuilder.pictures.size > index) {
                organizationBuilder.pictures.add(index, pictureId)
            } else {
                organizationBuilder.pictures.add(pictureId)
            }
        }
        return organizationBuilder
    }

    override fun <T> visit(visitor: OrganizationEventVisitor<T>): T {
        return visitor.visit(this)
    }

}