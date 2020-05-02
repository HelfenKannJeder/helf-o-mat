package de.helfenkannjeder.helfomat.api.organization.event

import de.helfenkannjeder.helfomat.core.organization.OrganizationId
import de.helfenkannjeder.helfomat.core.picture.PictureId

/**
 * @author Valentin Zickner
 */
data class OrganizationEditDeletePictureEventDto(
    override val organizationId: OrganizationId,
    val pictureId: PictureId
) : OrganizationEventDto {

    override fun <T> visit(visitor: OrganizationEventDtoVisitor<T>): T {
        return visitor.visit(this)
    }

}