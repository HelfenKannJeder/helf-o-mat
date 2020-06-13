package de.helfenkannjeder.helfomat.api.organization.event

/**
 * @author Valentin Zickner
 */
interface OrganizationEventDtoVisitor<T> {
    fun visit(organizationCreateEventDto: OrganizationCreateEventDto): T
    fun visit(organizationDeleteEventDto: OrganizationDeleteEventDto): T
    fun visit(organizationEditAddAddressEventDto: OrganizationEditAddAddressEventDto): T
    fun visit(organizationEditAddAttendanceTimeEventDto: OrganizationEditAddAttendanceTimeEventDto): T
    fun visit(organizationEditAddContactPersonEventDto: OrganizationEditAddContactPersonEventDto): T
    fun visit(organizationEditAddGroupEventDto: OrganizationEditAddGroupEventDto): T
    fun visit(organizationEditAddPictureEventDto: OrganizationEditAddPictureEventDto): T
    fun visit(organizationEditAddQuestionAnswerEventDto: OrganizationEditAddQuestionAnswerEventDto): T
    fun visit(organizationEditAddVolunteerEventDto: OrganizationEditAddVolunteerEventDto): T
    fun visit(organizationEditDefaultAddressEventDto: OrganizationEditDefaultAddressEventDto): T
    fun visit(organizationEditDeleteAddressEventDto: OrganizationEditDeleteAddressEventDto): T
    fun visit(organizationEditDeleteAttendanceTimeEventDto: OrganizationEditDeleteAttendanceTimeEventDto): T
    fun visit(organizationEditDeleteContactPersonEventDto: OrganizationEditDeleteContactPersonEventDto): T
    fun visit(organizationEditDeleteGroupEventDto: OrganizationEditDeleteGroupEventDto): T
    fun visit(organizationEditDeletePictureEventDto: OrganizationEditDeletePictureEventDto): T
    fun visit(organizationEditDeleteQuestionAnswerEventDto: OrganizationEditDeleteQuestionAnswerEventDto): T
    fun visit(organizationEditDeleteVolunteerEventDto: OrganizationEditDeleteVolunteerEventDto): T
    fun visit(organizationEditDescriptionEventDto: OrganizationEditDescriptionEventDto): T
    fun visit(organizationEditLogoEventDto: OrganizationEditLogoEventDto): T
    fun visit(organizationEditNameEventDto: OrganizationEditNameEventDto): T
    fun visit(organizationEditTeaserImageEventDto: OrganizationEditTeaserImageEventDto): T
    fun visit(organizationEditUrlNameEventDto: OrganizationEditUrlNameEventDto): T
    fun visit(organizationEditWebsiteEventDto: OrganizationEditWebsiteEventDto): T
    fun visit(proposedChangeOrganizationEventDto: ProposedChangeOrganizationEventDto): T
    fun visit(confirmedChangeOrganizationEventDto: ConfirmedChangeOrganizationEventDto): T
    fun visit(organizationEditChangeGroupEventDto: OrganizationEditChangeGroupEventDto): T
    fun visit(organizationEditChangePictureEventDto: OrganizationEditChangePictureEventDto): T
    fun visit(organizationEditChangeAttendanceTimeEventDto: OrganizationEditChangeAttendanceTimeEventDto): T
    fun visit(organizationEditChangeAddressEventDto: OrganizationEditChangeAddressEventDto): T
    fun visit(organizationEditChangeQuestionAnswerEventDto: OrganizationEditChangeQuestionAnswerEventDto): T
    fun visit(organizationEditChangeContactPersonEventDto: OrganizationEditChangeContactPersonEventDto): T
}