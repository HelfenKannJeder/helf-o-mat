package de.helfenkannjeder.helfomat.api.organization.event

import de.helfenkannjeder.helfomat.api.organization.*
import de.helfenkannjeder.helfomat.core.organization.event.*

/**
 * @author Valentin Zickner
 */
class OrganizationEventDtoAssembler : OrganizationEventDtoVisitor<OrganizationEvent> {

    override fun visit(organizationCreateEventDto: OrganizationCreateEventDto) =
        OrganizationCreateEvent(organizationCreateEventDto.organizationId, organizationCreateEventDto.name, organizationCreateEventDto.urlName, organizationCreateEventDto.organizationType)

    override fun visit(organizationDeleteEventDto: OrganizationDeleteEventDto) =
        OrganizationDeleteEvent(organizationDeleteEventDto.organizationId)

    override fun visit(organizationEditAddAddressEventDto: OrganizationEditAddAddressEventDto) =
        OrganizationEditAddAddressEvent(organizationEditAddAddressEventDto.organizationId, organizationEditAddAddressEventDto.index, organizationEditAddAddressEventDto.address.toAddress())

    override fun visit(organizationEditAddAttendanceTimeEventDto: OrganizationEditAddAttendanceTimeEventDto) =
        OrganizationEditAddAttendanceTimeEvent(organizationEditAddAttendanceTimeEventDto.organizationId, organizationEditAddAttendanceTimeEventDto.index, organizationEditAddAttendanceTimeEventDto.attendanceTime.toAttendanceTime())

    override fun visit(organizationEditAddContactPersonEventDto: OrganizationEditAddContactPersonEventDto) =
        OrganizationEditAddContactPersonEvent(organizationEditAddContactPersonEventDto.organizationId, organizationEditAddContactPersonEventDto.index, organizationEditAddContactPersonEventDto.contactPerson.toContactPerson())

    override fun visit(organizationEditAddGroupEventDto: OrganizationEditAddGroupEventDto) =
        OrganizationEditAddGroupEvent(organizationEditAddGroupEventDto.organizationId, organizationEditAddGroupEventDto.index, organizationEditAddGroupEventDto.group.toGroup())

    override fun visit(organizationEditAddPictureEventDto: OrganizationEditAddPictureEventDto) =
        OrganizationEditAddPictureEvent(organizationEditAddPictureEventDto.organizationId, organizationEditAddPictureEventDto.index, organizationEditAddPictureEventDto.pictureId)

    override fun visit(organizationEditAddQuestionAnswerEventDto: OrganizationEditAddQuestionAnswerEventDto) =
        OrganizationEditAddQuestionAnswerEvent(organizationEditAddQuestionAnswerEventDto.organizationId, organizationEditAddQuestionAnswerEventDto.index, organizationEditAddQuestionAnswerEventDto.answeredQuestion.toQuestionAnswer())

    override fun visit(organizationEditAddVolunteerEventDto: OrganizationEditAddVolunteerEventDto) =
        OrganizationEditAddVolunteerEvent(organizationEditAddVolunteerEventDto.organizationId, organizationEditAddVolunteerEventDto.index, organizationEditAddVolunteerEventDto.volunteer.toVolunteer())

    override fun visit(organizationEditDefaultAddressEventDto: OrganizationEditDefaultAddressEventDto) =
        OrganizationEditDefaultAddressEvent(organizationEditDefaultAddressEventDto.organizationId, organizationEditDefaultAddressEventDto.address?.toAddress())

    override fun visit(organizationEditDeleteAddressEventDto: OrganizationEditDeleteAddressEventDto) =
        OrganizationEditDefaultAddressEvent(organizationEditDeleteAddressEventDto.organizationId, organizationEditDeleteAddressEventDto.address.toAddress())

    override fun visit(organizationEditDeleteAttendanceTimeEventDto: OrganizationEditDeleteAttendanceTimeEventDto) =
        OrganizationEditDeleteAttendanceTimeEvent(organizationEditDeleteAttendanceTimeEventDto.organizationId, organizationEditDeleteAttendanceTimeEventDto.attendanceTime.toAttendanceTime())

    override fun visit(organizationEditDeleteContactPersonEventDto: OrganizationEditDeleteContactPersonEventDto) =
        OrganizationEditDeleteContactPersonEvent(organizationEditDeleteContactPersonEventDto.organizationId, organizationEditDeleteContactPersonEventDto.contactPerson.toContactPerson())

    override fun visit(organizationEditDeleteGroupEventDto: OrganizationEditDeleteGroupEventDto) =
        OrganizationEditDeleteGroupEvent(organizationEditDeleteGroupEventDto.organizationId, organizationEditDeleteGroupEventDto.group.toGroup())

    override fun visit(organizationEditDeletePictureEventDto: OrganizationEditDeletePictureEventDto) =
        OrganizationEditDeletePictureEvent(organizationEditDeletePictureEventDto.organizationId, organizationEditDeletePictureEventDto.pictureId)

    override fun visit(organizationEditDeleteQuestionAnswerEventDto: OrganizationEditDeleteQuestionAnswerEventDto) =
        OrganizationEditDeleteQuestionAnswerEvent(organizationEditDeleteQuestionAnswerEventDto.organizationId, organizationEditDeleteQuestionAnswerEventDto.answeredQuestion.toQuestionAnswer())

    override fun visit(organizationEditDeleteVolunteerEventDto: OrganizationEditDeleteVolunteerEventDto) =
        OrganizationEditDeleteVolunteerEvent(organizationEditDeleteVolunteerEventDto.organizationId, organizationEditDeleteVolunteerEventDto.volunteer.toVolunteer())

    override fun visit(organizationEditDescriptionEventDto: OrganizationEditDescriptionEventDto) =
        OrganizationEditDescriptionEvent(organizationEditDescriptionEventDto.organizationId, organizationEditDescriptionEventDto.description)

    override fun visit(organizationEditLogoEventDto: OrganizationEditLogoEventDto) =
        OrganizationEditLogoEvent(organizationEditLogoEventDto.organizationId, organizationEditLogoEventDto.logo)

    override fun visit(organizationEditNameEventDto: OrganizationEditNameEventDto) =
        OrganizationEditNameEvent(organizationEditNameEventDto.organizationId, organizationEditNameEventDto.name)

    override fun visit(organizationEditTeaserImageEventDto: OrganizationEditTeaserImageEventDto) =
        OrganizationEditTeaserImageEvent(organizationEditTeaserImageEventDto.organizationId, organizationEditTeaserImageEventDto.teaserImage)

    override fun visit(organizationEditUrlNameEventDto: OrganizationEditUrlNameEventDto) =
        OrganizationEditUrlNameEvent(organizationEditUrlNameEventDto.organizationId, organizationEditUrlNameEventDto.urlName)

    override fun visit(organizationEditWebsiteEventDto: OrganizationEditWebsiteEventDto) =
        OrganizationEditWebsiteEvent(organizationEditWebsiteEventDto.organizationId, organizationEditWebsiteEventDto.website)

    override fun visit(organizationEditChangeGroupEventDto: OrganizationEditChangeGroupEventDto): OrganizationEvent =
        OrganizationEditChangeGroupEvent(organizationEditChangeGroupEventDto.organizationId, organizationEditChangeGroupEventDto.indexOffset, organizationEditChangeGroupEventDto.oldGroup.toGroup(), organizationEditChangeGroupEventDto.group.toGroup())

    override fun visit(organizationEditChangePictureEventDto: OrganizationEditChangePictureEventDto): OrganizationEvent =
        OrganizationEditChangePictureEvent(organizationEditChangePictureEventDto.organizationId, organizationEditChangePictureEventDto.indexOffset, organizationEditChangePictureEventDto.pictureId)

    override fun visit(organizationEditChangeAttendanceTimeEventDto: OrganizationEditChangeAttendanceTimeEventDto): OrganizationEvent =
        OrganizationEditChangeAttendanceTimeEvent(organizationEditChangeAttendanceTimeEventDto.organizationId, organizationEditChangeAttendanceTimeEventDto.indexOffset, organizationEditChangeAttendanceTimeEventDto.oldAttendanceTime.toAttendanceTime(), organizationEditChangeAttendanceTimeEventDto.attendanceTime.toAttendanceTime())

    override fun visit(organizationEditChangeAddressEventDto: OrganizationEditChangeAddressEventDto): OrganizationEvent =
        OrganizationEditChangeAddressEvent(organizationEditChangeAddressEventDto.organizationId, organizationEditChangeAddressEventDto.indexOffset, organizationEditChangeAddressEventDto.oldAddress.toAddress(), organizationEditChangeAddressEventDto.address.toAddress())

    override fun visit(proposedChangeOrganizationEventDto: ProposedChangeOrganizationEventDto) =
        ProposedChangeOrganizationEvent(
            proposedChangeOrganizationEventDto.organizationId,
            proposedChangeOrganizationEventDto.author,
            proposedChangeOrganizationEventDto.sources,
            proposedChangeOrganizationEventDto.changes.map { it.visit(this) }
        )

    override fun visit(confirmedChangeOrganizationEventDto: ConfirmedChangeOrganizationEventDto) =
        ConfirmedChangeOrganizationEvent(
            confirmedChangeOrganizationEventDto.organizationId,
            confirmedChangeOrganizationEventDto.approvedBy,
            confirmedChangeOrganizationEventDto.author,
            confirmedChangeOrganizationEventDto.sources,
            confirmedChangeOrganizationEventDto.changes.map { it.visit(this) }
        )

    companion object {
        fun toOrganizationEvent(organizationEvents: List<OrganizationEventDto>): List<OrganizationEvent> {
            val organizationEventDtoAssembler = OrganizationEventDtoAssembler()
            return organizationEvents.map { it.visit(organizationEventDtoAssembler) }
        }
    }
}