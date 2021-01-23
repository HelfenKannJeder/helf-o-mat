package de.helfenkannjeder.helfomat.api.organization.event

import de.helfenkannjeder.helfomat.api.organization.*
import de.helfenkannjeder.helfomat.core.organization.Organization
import de.helfenkannjeder.helfomat.core.organization.OrganizationEventVisitor
import de.helfenkannjeder.helfomat.core.organization.event.*
import de.helfenkannjeder.helfomat.core.question.Question

/**
 * @author Valentin Zickner
 */
class OrganizationEventAssembler(
    private val questions: List<Question>,
    originalOrganization: Organization?
) : OrganizationEventVisitor<OrganizationEventDto> {

    private var currentOrganization: Organization? = originalOrganization?.copy()

    override fun visit(organizationCreateEvent: OrganizationCreateEvent) =
        OrganizationCreateEventDto(organizationCreateEvent.organizationId, organizationCreateEvent.name, organizationCreateEvent.urlName, organizationCreateEvent.organizationType, isEventApplicable(organizationCreateEvent))

    override fun visit(organizationDeleteEvent: OrganizationDeleteEvent) =
        OrganizationDeleteEventDto(organizationDeleteEvent.organizationId, isEventApplicable(organizationDeleteEvent))

    override fun visit(organizationEditAddAddressEvent: OrganizationEditAddAddressEvent) =
        OrganizationEditAddAddressEventDto(organizationEditAddAddressEvent.organizationId, organizationEditAddAddressEvent.index, organizationEditAddAddressEvent.address.toAddressDto(), isEventApplicable(organizationEditAddAddressEvent))

    override fun visit(organizationEditAddAttendanceTimeEvent: OrganizationEditAddAttendanceTimeEvent) =
        OrganizationEditAddAttendanceTimeEventDto(organizationEditAddAttendanceTimeEvent.organizationId, organizationEditAddAttendanceTimeEvent.index, organizationEditAddAttendanceTimeEvent.attendanceTime.toAttendanceTimeDto(), isEventApplicable(organizationEditAddAttendanceTimeEvent))

    override fun visit(organizationEditAddContactPersonEvent: OrganizationEditAddContactPersonEvent) =
        OrganizationEditAddContactPersonEventDto(organizationEditAddContactPersonEvent.organizationId, organizationEditAddContactPersonEvent.index, organizationEditAddContactPersonEvent.contactPerson.toContactPersonDto(), isEventApplicable(organizationEditAddContactPersonEvent))

    override fun visit(organizationEditAddGroupEvent: OrganizationEditAddGroupEvent) =
        OrganizationEditAddGroupEventDto(organizationEditAddGroupEvent.organizationId, organizationEditAddGroupEvent.index, organizationEditAddGroupEvent.group.toGroupDto(), isEventApplicable(organizationEditAddGroupEvent))

    override fun visit(organizationEditAddPictureEvent: OrganizationEditAddPictureEvent) =
        OrganizationEditAddPictureEventDto(organizationEditAddPictureEvent.organizationId, organizationEditAddPictureEvent.index, organizationEditAddPictureEvent.pictureId, isEventApplicable(organizationEditAddPictureEvent))

    override fun visit(organizationEditAddQuestionAnswerEvent: OrganizationEditAddQuestionAnswerEvent) =
        OrganizationEditAddQuestionAnswerEventDto(
            organizationEditAddQuestionAnswerEvent.organizationId,
            organizationEditAddQuestionAnswerEvent.index,
            organizationEditAddQuestionAnswerEvent.questionAnswer.toAnsweredQuestionDto(questions.getAnswerToQuestion(organizationEditAddQuestionAnswerEvent.questionAnswer.questionId)),
            isEventApplicable(organizationEditAddQuestionAnswerEvent)
        )

    override fun visit(organizationEditAddVolunteerEvent: OrganizationEditAddVolunteerEvent) =
        OrganizationEditAddVolunteerEventDto(organizationEditAddVolunteerEvent.organizationId, organizationEditAddVolunteerEvent.index, organizationEditAddVolunteerEvent.volunteer.toVolunteerDto(), isEventApplicable(organizationEditAddVolunteerEvent))

    override fun visit(organizationEditDefaultAddressEvent: OrganizationEditDefaultAddressEvent) =
        OrganizationEditDefaultAddressEventDto(organizationEditDefaultAddressEvent.organizationId, organizationEditDefaultAddressEvent.defaultAddress?.toAddressDto(), isEventApplicable(organizationEditDefaultAddressEvent))

    override fun visit(organizationEditDeleteAddressEvent: OrganizationEditDeleteAddressEvent) =
        OrganizationEditDeleteAddressEventDto(organizationEditDeleteAddressEvent.organizationId, organizationEditDeleteAddressEvent.address.toAddressDto(), isEventApplicable(organizationEditDeleteAddressEvent))

    override fun visit(organizationEditDeleteAttendanceTimeEvent: OrganizationEditDeleteAttendanceTimeEvent) =
        OrganizationEditDeleteAttendanceTimeEventDto(organizationEditDeleteAttendanceTimeEvent.organizationId, organizationEditDeleteAttendanceTimeEvent.attendanceTime.toAttendanceTimeDto(), isEventApplicable(organizationEditDeleteAttendanceTimeEvent))

    override fun visit(organizationEditDeleteContactPersonEvent: OrganizationEditDeleteContactPersonEvent) =
        OrganizationEditDeleteContactPersonEventDto(organizationEditDeleteContactPersonEvent.organizationId, organizationEditDeleteContactPersonEvent.contactPerson.toContactPersonDto(), isEventApplicable(organizationEditDeleteContactPersonEvent))

    override fun visit(organizationEditDeleteGroupEvent: OrganizationEditDeleteGroupEvent) =
        OrganizationEditDeleteGroupEventDto(organizationEditDeleteGroupEvent.organizationId, organizationEditDeleteGroupEvent.group.toGroupDto(), isEventApplicable(organizationEditDeleteGroupEvent))

    override fun visit(organizationEditDeletePictureEvent: OrganizationEditDeletePictureEvent) =
        OrganizationEditDeletePictureEventDto(organizationEditDeletePictureEvent.organizationId, organizationEditDeletePictureEvent.pictureId, isEventApplicable(organizationEditDeletePictureEvent))

    override fun visit(organizationEditDeleteQuestionAnswerEvent: OrganizationEditDeleteQuestionAnswerEvent) =
        OrganizationEditDeleteQuestionAnswerEventDto(
            organizationEditDeleteQuestionAnswerEvent.organizationId,
            organizationEditDeleteQuestionAnswerEvent.questionAnswer.toAnsweredQuestionDto(questions.getAnswerToQuestion(organizationEditDeleteQuestionAnswerEvent.questionAnswer.questionId)),
            isEventApplicable(organizationEditDeleteQuestionAnswerEvent)
        )

    override fun visit(organizationEditDeleteVolunteerEvent: OrganizationEditDeleteVolunteerEvent) =
        OrganizationEditDeleteVolunteerEventDto(organizationEditDeleteVolunteerEvent.organizationId, organizationEditDeleteVolunteerEvent.volunteer.toVolunteerDto(), isEventApplicable(organizationEditDeleteVolunteerEvent))

    override fun visit(organizationEditDescriptionEvent: OrganizationEditDescriptionEvent) =
        OrganizationEditDescriptionEventDto(organizationEditDescriptionEvent.organizationId, organizationEditDescriptionEvent.description, isEventApplicable(organizationEditDescriptionEvent))

    override fun visit(organizationEditLogoEvent: OrganizationEditLogoEvent) =
        OrganizationEditLogoEventDto(organizationEditLogoEvent.organizationId, organizationEditLogoEvent.logo, isEventApplicable(organizationEditLogoEvent))

    override fun visit(organizationEditNameEvent: OrganizationEditNameEvent) =
        OrganizationEditNameEventDto(organizationEditNameEvent.organizationId, organizationEditNameEvent.name, isEventApplicable(organizationEditNameEvent))

    override fun visit(organizationEditTeaserImageEvent: OrganizationEditTeaserImageEvent) =
        OrganizationEditTeaserImageEventDto(organizationEditTeaserImageEvent.organizationId, organizationEditTeaserImageEvent.teaserImage, isEventApplicable(organizationEditTeaserImageEvent))

    override fun visit(organizationEditUrlNameEvent: OrganizationEditUrlNameEvent) =
        OrganizationEditUrlNameEventDto(organizationEditUrlNameEvent.organizationId, organizationEditUrlNameEvent.urlName, isEventApplicable(organizationEditUrlNameEvent))

    override fun visit(organizationEditWebsiteEvent: OrganizationEditWebsiteEvent) =
        OrganizationEditWebsiteEventDto(organizationEditWebsiteEvent.organizationId, organizationEditWebsiteEvent.website, isEventApplicable(organizationEditWebsiteEvent))

    override fun visit(proposedChangeOrganizationEvent: ProposedChangeOrganizationEvent) =
        ProposedChangeOrganizationEventDto(
            proposedChangeOrganizationEvent.organizationId,
            proposedChangeOrganizationEvent.author,
            proposedChangeOrganizationEvent.sources,
            proposedChangeOrganizationEvent.changes.map { it.visit(this) },
            true
        )

    override fun visit(confirmedChangeOrganizationEvent: ConfirmedChangeOrganizationEvent) =
        ConfirmedChangeOrganizationEventDto(
            confirmedChangeOrganizationEvent.organizationId,
            confirmedChangeOrganizationEvent.approvedBy,
            confirmedChangeOrganizationEvent.author,
            confirmedChangeOrganizationEvent.sources,
            confirmedChangeOrganizationEvent.changes.map { it.visit(this) },
            true
        )

    override fun visit(organizationEditChangeGroupEvent: OrganizationEditChangeGroupEvent): OrganizationEventDto =
        OrganizationEditChangeGroupEventDto(
            organizationEditChangeGroupEvent.organizationId,
            organizationEditChangeGroupEvent.indexOffset,
            organizationEditChangeGroupEvent.oldGroup.toGroupDto(),
            organizationEditChangeGroupEvent.group.toGroupDto(),
            isEventApplicable(organizationEditChangeGroupEvent)
        )

    override fun visit(organizationEditChangePictureEvent: OrganizationEditChangePictureEvent): OrganizationEventDto =
        OrganizationEditChangePictureEventDto(
            organizationEditChangePictureEvent.organizationId,
            organizationEditChangePictureEvent.indexOffset,
            organizationEditChangePictureEvent.pictureId,
            isEventApplicable(organizationEditChangePictureEvent)
        )

    override fun visit(organizationEditChangeAttendanceTimeEvent: OrganizationEditChangeAttendanceTimeEvent): OrganizationEventDto =
        OrganizationEditChangeAttendanceTimeEventDto(
            organizationEditChangeAttendanceTimeEvent.organizationId,
            organizationEditChangeAttendanceTimeEvent.indexOffset,
            organizationEditChangeAttendanceTimeEvent.oldAttendanceTime.toAttendanceTimeDto(),
            organizationEditChangeAttendanceTimeEvent.attendanceTime.toAttendanceTimeDto(),
            isEventApplicable(organizationEditChangeAttendanceTimeEvent)
        )

    override fun visit(organizationEditChangeAddressEvent: OrganizationEditChangeAddressEvent): OrganizationEventDto =
        OrganizationEditChangeAddressEventDto(
            organizationEditChangeAddressEvent.organizationId,
            organizationEditChangeAddressEvent.indexOffset,
            organizationEditChangeAddressEvent.oldAddress.toAddressDto(),
            organizationEditChangeAddressEvent.address.toAddressDto(),
            isEventApplicable(organizationEditChangeAddressEvent)
        )

    override fun visit(organizationEditChangeQuestionAnswerEvent: OrganizationEditChangeQuestionAnswerEvent): OrganizationEventDto =
        OrganizationEditChangeQuestionAnswerEventDto(
            organizationEditChangeQuestionAnswerEvent.organizationId,
            organizationEditChangeQuestionAnswerEvent.indexOffset,
            organizationEditChangeQuestionAnswerEvent.oldQuestionAnswer.toAnsweredQuestionDto(questions.getAnswerToQuestion(organizationEditChangeQuestionAnswerEvent.oldQuestionAnswer.questionId)),
            organizationEditChangeQuestionAnswerEvent.questionAnswer.toAnsweredQuestionDto(questions.getAnswerToQuestion(organizationEditChangeQuestionAnswerEvent.questionAnswer.questionId)),
            isEventApplicable(organizationEditChangeQuestionAnswerEvent)
        )

    override fun visit(organizationEditChangeContactPersonEvent: OrganizationEditChangeContactPersonEvent): OrganizationEventDto =
        OrganizationEditChangeContactPersonEventDto(
            organizationEditChangeContactPersonEvent.organizationId,
            organizationEditChangeContactPersonEvent.indexOffset,
            organizationEditChangeContactPersonEvent.oldContactPerson.toContactPersonDto(),
            organizationEditChangeContactPersonEvent.contactPerson.toContactPersonDto(),
            isEventApplicable(organizationEditChangeContactPersonEvent)
        )

    private fun isEventApplicable(organizationEvent: OrganizationEvent): Boolean {
        val organization = currentOrganization ?: return true
        val builder = Organization.Builder(organization)
        organizationEvent.applyOnOrganizationBuilder(builder)
        val newOrganization = builder.build()
        val isApplicable = organization != newOrganization
        currentOrganization = newOrganization
        return isApplicable
    }
}

fun OrganizationEvent.toOrganizationEventDto(questions: List<Question>, originalOrganization: Organization?) = this.toOrganizationEventDto(OrganizationEventAssembler(questions, originalOrganization))
fun OrganizationEvent.toOrganizationEventDto(organizationEventAssembler: OrganizationEventAssembler) = this.visit(organizationEventAssembler)
fun List<OrganizationEvent>.toOrganizationEventDtos(questions: List<Question>, originalOrganization: Organization?): List<OrganizationEventDto> {
    val organizationEventAssembler = OrganizationEventAssembler(questions, originalOrganization)
    return this.map { it.toOrganizationEventDto(organizationEventAssembler) }
}