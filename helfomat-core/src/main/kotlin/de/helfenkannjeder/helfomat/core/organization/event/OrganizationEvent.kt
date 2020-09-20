package de.helfenkannjeder.helfomat.core.organization.event

import com.fasterxml.jackson.annotation.JsonTypeInfo
import de.helfenkannjeder.helfomat.core.event.DomainEvent
import de.helfenkannjeder.helfomat.core.organization.NullableOrganizationEventVisitor
import de.helfenkannjeder.helfomat.core.organization.Organization
import de.helfenkannjeder.helfomat.core.organization.OrganizationEventVisitor
import de.helfenkannjeder.helfomat.core.organization.OrganizationId
import de.helfenkannjeder.helfomat.core.picture.PictureId

/**
 * @author Valentin Zickner
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, property = "type")
abstract class OrganizationEvent(
    open val organizationId: OrganizationId
) : DomainEvent() {

    abstract fun applyOnOrganizationBuilder(organizationBuilder: Organization.Builder?): Organization.Builder?
    abstract fun <T> visit(visitor: OrganizationEventVisitor<T>): T

    companion object {

        fun toPictureIds(events: List<OrganizationEvent>): List<PictureId> {
            val visitor = object : NullableOrganizationEventVisitor<List<PictureId>> {
                override fun visit(organizationEditAddPictureEvent: OrganizationEditAddPictureEvent): List<PictureId>? =
                    listOf(organizationEditAddPictureEvent.pictureId)

                override fun visit(organizationEditTeaserImageEvent: OrganizationEditTeaserImageEvent): List<PictureId>? {
                    val pictureId = organizationEditTeaserImageEvent.teaserImage ?: return null
                    return listOf(pictureId)
                }

                override fun visit(organizationEditAddContactPersonEvent: OrganizationEditAddContactPersonEvent): List<PictureId>? {
                    val pictureId = organizationEditAddContactPersonEvent.contactPerson.picture ?: return null
                    return listOf(pictureId)
                }

                override fun visit(organizationEditAddVolunteerEvent: OrganizationEditAddVolunteerEvent): List<PictureId>? {
                    val pictureId = organizationEditAddVolunteerEvent.volunteer.picture ?: return null
                    return listOf(pictureId)
                }

                override fun visit(organizationEditLogoEvent: OrganizationEditLogoEvent): List<PictureId>? {
                    val pictureId = organizationEditLogoEvent.logo ?: return null
                    return listOf(pictureId)
                }

                override fun visit(organizationEditChangeContactPersonEvent: OrganizationEditChangeContactPersonEvent): List<PictureId>? {
                    val pictureId = organizationEditChangeContactPersonEvent.contactPerson.picture ?: return null
                    return listOf(pictureId)
                }
            }
            return events.flatMap { it.visit(visitor) ?: emptyList() }
        }

    }

}