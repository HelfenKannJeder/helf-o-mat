package de.helfenkannjeder.helfomat.api.organization.event

import de.helfenkannjeder.helfomat.core.organization.OrganizationId
import de.helfenkannjeder.helfomat.core.picture.PictureId

class OrganizationEditChangePictureEventDto(
    override val organizationId: OrganizationId,
    val indexOffset: Int,
    val pictureId: PictureId,
    override val eventApplicable: Boolean
) : OrganizationEventDto {

    override fun <T> visit(visitor: OrganizationEventDtoVisitor<T>): T {
        return visitor.visit(this)
    }

}