package de.helfenkannjeder.helfomat.core.organization

import de.helfenkannjeder.helfomat.core.organization.event.*

interface OrganizationEventVisitor<T> {
    fun visit(organizationCreateEvent: OrganizationCreateEvent): T
    fun visit(organizationDeleteEvent: OrganizationDeleteEvent): T
    fun visit(organizationEditAddAddressEvent: OrganizationEditAddAddressEvent): T
    fun visit(organizationEditAddAttendanceTimeEvent: OrganizationEditAddAttendanceTimeEvent): T
    fun visit(organizationEditAddContactPersonEvent: OrganizationEditAddContactPersonEvent): T
    fun visit(organizationEditAddGroupEvent: OrganizationEditAddGroupEvent): T
    fun visit(organizationEditAddPictureEvent: OrganizationEditAddPictureEvent): T
    fun visit(organizationEditAddQuestionAnswerEvent: OrganizationEditAddQuestionAnswerEvent): T
    fun visit(organizationEditAddVolunteerEvent: OrganizationEditAddVolunteerEvent): T
    fun visit(organizationEditDefaultAddressEvent: OrganizationEditDefaultAddressEvent): T
    fun visit(organizationEditDeleteAddressEvent: OrganizationEditDeleteAddressEvent): T
    fun visit(organizationEditDeleteAttendanceTimeEvent: OrganizationEditDeleteAttendanceTimeEvent): T
    fun visit(organizationEditDeleteContactPersonEvent: OrganizationEditDeleteContactPersonEvent): T
    fun visit(organizationEditDeleteGroupEvent: OrganizationEditDeleteGroupEvent): T
    fun visit(organizationEditDeletePictureEvent: OrganizationEditDeletePictureEvent): T
    fun visit(organizationEditDeleteQuestionAnswerEvent: OrganizationEditDeleteQuestionAnswerEvent): T
    fun visit(organizationEditDeleteVolunteerEvent: OrganizationEditDeleteVolunteerEvent): T
    fun visit(organizationEditDescriptionEvent: OrganizationEditDescriptionEvent): T
    fun visit(organizationEditLogoEvent: OrganizationEditLogoEvent): T
    fun visit(organizationEditNameEvent: OrganizationEditNameEvent): T
    fun visit(organizationEditTeaserImageEvent: OrganizationEditTeaserImageEvent): T
    fun visit(organizationEditUrlNameEvent: OrganizationEditUrlNameEvent): T
    fun visit(organizationEditWebsiteEvent: OrganizationEditWebsiteEvent): T
    fun visit(proposedChangeOrganizationEvent: ProposedChangeOrganizationEvent): T
    fun visit(confirmedChangeOrganizationEvent: ConfirmedChangeOrganizationEvent): T
    fun visit(organizationEditChangeGroupEvent: OrganizationEditChangeGroupEvent): T
    fun visit(organizationEditChangePictureEvent: OrganizationEditChangePictureEvent): T
    fun visit(organizationEditChangeAttendanceTimeEvent: OrganizationEditChangeAttendanceTimeEvent): T
    fun visit(organizationEditChangeAddressEvent: OrganizationEditChangeAddressEvent): T
}