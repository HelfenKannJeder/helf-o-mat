package de.helfenkannjeder.helfomat.api.organization.event

import de.helfenkannjeder.helfomat.api.organization.*
import de.helfenkannjeder.helfomat.core.organization.OrganizationEventVisitor
import de.helfenkannjeder.helfomat.core.organization.event.*
import de.helfenkannjeder.helfomat.core.question.Question

/**
 * @author Valentin Zickner
 */
class OrganizationEventAssembler(private val questions: List<Question>) : OrganizationEventVisitor<OrganizationEventDto> {

    override fun visit(organizationCreateEvent: OrganizationCreateEvent) =
        OrganizationCreateEventDto(organizationCreateEvent.organizationId, organizationCreateEvent.name, organizationCreateEvent.urlName, organizationCreateEvent.organizationType)

    override fun visit(organizationDeleteEvent: OrganizationDeleteEvent) =
        OrganizationDeleteEventDto(organizationDeleteEvent.organizationId)

    override fun visit(organizationEditAddAddressEvent: OrganizationEditAddAddressEvent) =
        OrganizationEditAddAddressEventDto(organizationEditAddAddressEvent.organizationId, organizationEditAddAddressEvent.index, organizationEditAddAddressEvent.address.toAddressDto())

    override fun visit(organizationEditAddAttendanceTimeEvent: OrganizationEditAddAttendanceTimeEvent) =
        OrganizationEditAddAttendanceTimeEventDto(organizationEditAddAttendanceTimeEvent.organizationId, organizationEditAddAttendanceTimeEvent.index, organizationEditAddAttendanceTimeEvent.attendanceTime.toAttendanceTimeDto())

    override fun visit(organizationEditAddContactPersonEvent: OrganizationEditAddContactPersonEvent) =
        OrganizationEditAddContactPersonEventDto(organizationEditAddContactPersonEvent.organizationId, organizationEditAddContactPersonEvent.index, organizationEditAddContactPersonEvent.contactPerson.toContactPersonDto())

    override fun visit(organizationEditAddGroupEvent: OrganizationEditAddGroupEvent) =
        OrganizationEditAddGroupEventDto(organizationEditAddGroupEvent.organizationId, organizationEditAddGroupEvent.index, organizationEditAddGroupEvent.group.toGroupDto())

    override fun visit(organizationEditAddPictureEvent: OrganizationEditAddPictureEvent) =
        OrganizationEditAddPictureEventDto(organizationEditAddPictureEvent.organizationId, organizationEditAddPictureEvent.index, organizationEditAddPictureEvent.pictureId)

    override fun visit(organizationEditAddQuestionAnswerEvent: OrganizationEditAddQuestionAnswerEvent) =
        OrganizationEditAddQuestionAnswerEventDto(
            organizationEditAddQuestionAnswerEvent.organizationId,
            organizationEditAddQuestionAnswerEvent.index,
            organizationEditAddQuestionAnswerEvent.questionAnswer.toAnsweredQuestionDto(questions.getAnswerToQuestion(organizationEditAddQuestionAnswerEvent.questionAnswer.questionId))
        )

    override fun visit(organizationEditAddVolunteerEvent: OrganizationEditAddVolunteerEvent) =
        OrganizationEditAddVolunteerEventDto(organizationEditAddVolunteerEvent.organizationId, organizationEditAddVolunteerEvent.index, organizationEditAddVolunteerEvent.volunteer.toVolunteerDto())

    override fun visit(organizationEditDefaultAddressEvent: OrganizationEditDefaultAddressEvent) =
        OrganizationEditDefaultAddressEventDto(organizationEditDefaultAddressEvent.organizationId, organizationEditDefaultAddressEvent.defaultAddress?.toAddressDto())

    override fun visit(organizationEditDeleteAddressEvent: OrganizationEditDeleteAddressEvent) =
        OrganizationEditDeleteAddressEventDto(organizationEditDeleteAddressEvent.organizationId, organizationEditDeleteAddressEvent.address.toAddressDto())

    override fun visit(organizationEditDeleteAttendanceTimeEvent: OrganizationEditDeleteAttendanceTimeEvent) =
        OrganizationEditDeleteAttendanceTimeEventDto(organizationEditDeleteAttendanceTimeEvent.organizationId, organizationEditDeleteAttendanceTimeEvent.attendanceTime.toAttendanceTimeDto())

    override fun visit(organizationEditDeleteContactPersonEvent: OrganizationEditDeleteContactPersonEvent) =
        OrganizationEditDeleteContactPersonEventDto(organizationEditDeleteContactPersonEvent.organizationId, organizationEditDeleteContactPersonEvent.contactPerson.toContactPersonDto())

    override fun visit(organizationEditDeleteGroupEvent: OrganizationEditDeleteGroupEvent) =
        OrganizationEditDeleteGroupEventDto(organizationEditDeleteGroupEvent.organizationId, organizationEditDeleteGroupEvent.group.toGroupDto())

    override fun visit(organizationEditDeletePictureEvent: OrganizationEditDeletePictureEvent) =
        OrganizationEditDeletePictureEventDto(organizationEditDeletePictureEvent.organizationId, organizationEditDeletePictureEvent.pictureId)

    override fun visit(organizationEditDeleteQuestionAnswerEvent: OrganizationEditDeleteQuestionAnswerEvent) =
        OrganizationEditDeleteQuestionAnswerEventDto(
            organizationEditDeleteQuestionAnswerEvent.organizationId,
            organizationEditDeleteQuestionAnswerEvent.questionAnswer.toAnsweredQuestionDto(questions.getAnswerToQuestion(organizationEditDeleteQuestionAnswerEvent.questionAnswer.questionId))
        )

    override fun visit(organizationEditDeleteVolunteerEvent: OrganizationEditDeleteVolunteerEvent) =
        OrganizationEditDeleteVolunteerEventDto(organizationEditDeleteVolunteerEvent.organizationId, organizationEditDeleteVolunteerEvent.volunteer.toVolunteerDto())

    override fun visit(organizationEditDescriptionEvent: OrganizationEditDescriptionEvent) =
        OrganizationEditDescriptionEventDto(organizationEditDescriptionEvent.organizationId, organizationEditDescriptionEvent.description)

    override fun visit(organizationEditLogoEvent: OrganizationEditLogoEvent) =
        OrganizationEditLogoEventDto(organizationEditLogoEvent.organizationId, organizationEditLogoEvent.logo)

    override fun visit(organizationEditNameEvent: OrganizationEditNameEvent) =
        OrganizationEditNameEventDto(organizationEditNameEvent.organizationId, organizationEditNameEvent.name)

    override fun visit(organizationEditTeaserImageEvent: OrganizationEditTeaserImageEvent) =
        OrganizationEditTeaserImageEventDto(organizationEditTeaserImageEvent.organizationId, organizationEditTeaserImageEvent.teaserImage)

    override fun visit(organizationEditUrlNameEvent: OrganizationEditUrlNameEvent) =
        OrganizationEditUrlNameEventDto(organizationEditUrlNameEvent.organizationId, organizationEditUrlNameEvent.urlName)

    override fun visit(organizationEditWebsiteEvent: OrganizationEditWebsiteEvent) =
        OrganizationEditWebsiteEventDto(organizationEditWebsiteEvent.organizationId, organizationEditWebsiteEvent.website)

    override fun visit(proposedChangeOrganizationEvent: ProposedChangeOrganizationEvent) =
        ProposedChangeOrganizationEventDto(
            proposedChangeOrganizationEvent.organizationId,
            proposedChangeOrganizationEvent.author,
            proposedChangeOrganizationEvent.sources,
            proposedChangeOrganizationEvent.changes.map { it.visit(this) }
        )

    override fun visit(confirmedChangeOrganizationEvent: ConfirmedChangeOrganizationEvent) =
        ConfirmedChangeOrganizationEventDto(
            confirmedChangeOrganizationEvent.organizationId,
            confirmedChangeOrganizationEvent.approvedBy,
            confirmedChangeOrganizationEvent.author,
            confirmedChangeOrganizationEvent.sources,
            confirmedChangeOrganizationEvent.changes.map { it.visit(this) }
        )

    override fun visit(organizationEditChangeGroupEvent: OrganizationEditChangeGroupEvent): OrganizationEventDto =
        OrganizationEditChangeGroupEventDto(
            organizationEditChangeGroupEvent.organizationId,
            organizationEditChangeGroupEvent.indexOffset,
            organizationEditChangeGroupEvent.oldGroup.toGroupDto(),
            organizationEditChangeGroupEvent.group.toGroupDto()
        )

    override fun visit(organizationEditChangePictureEvent: OrganizationEditChangePictureEvent): OrganizationEventDto =
        OrganizationEditChangePictureEventDto(
            organizationEditChangePictureEvent.organizationId,
            organizationEditChangePictureEvent.indexOffset,
            organizationEditChangePictureEvent.pictureId
        )

    override fun visit(organizationEditChangeAttendanceTimeEvent: OrganizationEditChangeAttendanceTimeEvent): OrganizationEventDto =
        OrganizationEditChangeAttendanceTimeEventDto(
            organizationEditChangeAttendanceTimeEvent.organizationId,
            organizationEditChangeAttendanceTimeEvent.indexOffset,
            organizationEditChangeAttendanceTimeEvent.oldAttendanceTime.toAttendanceTimeDto(),
            organizationEditChangeAttendanceTimeEvent.attendanceTime.toAttendanceTimeDto()
        )

    override fun visit(organizationEditChangeAddressEvent: OrganizationEditChangeAddressEvent): OrganizationEventDto =
        OrganizationEditChangeAddressEventDto(
            organizationEditChangeAddressEvent.organizationId,
            organizationEditChangeAddressEvent.indexOffset,
            organizationEditChangeAddressEvent.oldAddress.toAddressDto(),
            organizationEditChangeAddressEvent.address.toAddressDto()
        )

    override fun visit(organizationEditChangeQuestionAnswerEvent: OrganizationEditChangeQuestionAnswerEvent): OrganizationEventDto {
        return OrganizationEditChangeQuestionAnswerEventDto(
            organizationEditChangeQuestionAnswerEvent.organizationId,
            organizationEditChangeQuestionAnswerEvent.indexOffset,
            organizationEditChangeQuestionAnswerEvent.oldQuestionAnswer.toAnsweredQuestionDto(questions.getAnswerToQuestion(organizationEditChangeQuestionAnswerEvent.oldQuestionAnswer.questionId)),
            organizationEditChangeQuestionAnswerEvent.questionAnswer.toAnsweredQuestionDto(questions.getAnswerToQuestion(organizationEditChangeQuestionAnswerEvent.questionAnswer.questionId))
        )
    }
}

fun OrganizationEvent.toOrganizationEventDto(questions: List<Question>) = this.toOrganizationEventDto(OrganizationEventAssembler(questions))
fun OrganizationEvent.toOrganizationEventDto(organizationEventAssembler: OrganizationEventAssembler) = this.visit(organizationEventAssembler)
fun List<OrganizationEvent>.toOrganizationEventDtos(questions: List<Question>): List<OrganizationEventDto> {
    val organizationEventAssembler = OrganizationEventAssembler(questions)
    return this.map { it.toOrganizationEventDto(organizationEventAssembler) }
}