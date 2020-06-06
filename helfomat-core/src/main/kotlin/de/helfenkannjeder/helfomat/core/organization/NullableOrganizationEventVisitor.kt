package de.helfenkannjeder.helfomat.core.organization

import de.helfenkannjeder.helfomat.core.organization.event.*

interface NullableOrganizationEventVisitor<T> : OrganizationEventVisitor<T?> {
    override fun visit(organizationCreateEvent: OrganizationCreateEvent): T? = null
    override fun visit(organizationDeleteEvent: OrganizationDeleteEvent): T? = null
    override fun visit(organizationEditAddAddressEvent: OrganizationEditAddAddressEvent): T? = null
    override fun visit(organizationEditAddAttendanceTimeEvent: OrganizationEditAddAttendanceTimeEvent): T? = null
    override fun visit(organizationEditAddContactPersonEvent: OrganizationEditAddContactPersonEvent): T? = null
    override fun visit(organizationEditAddGroupEvent: OrganizationEditAddGroupEvent): T? = null
    override fun visit(organizationEditAddPictureEvent: OrganizationEditAddPictureEvent): T? = null
    override fun visit(organizationEditAddQuestionAnswerEvent: OrganizationEditAddQuestionAnswerEvent): T? = null
    override fun visit(organizationEditAddVolunteerEvent: OrganizationEditAddVolunteerEvent): T? = null
    override fun visit(organizationEditDefaultAddressEvent: OrganizationEditDefaultAddressEvent): T? = null
    override fun visit(organizationEditDeleteAddressEvent: OrganizationEditDeleteAddressEvent): T? = null
    override fun visit(organizationEditDeleteAttendanceTimeEvent: OrganizationEditDeleteAttendanceTimeEvent): T? = null
    override fun visit(organizationEditDeleteContactPersonEvent: OrganizationEditDeleteContactPersonEvent): T? = null
    override fun visit(organizationEditDeleteGroupEvent: OrganizationEditDeleteGroupEvent): T? = null
    override fun visit(organizationEditDeletePictureEvent: OrganizationEditDeletePictureEvent): T? = null
    override fun visit(organizationEditDeleteQuestionAnswerEvent: OrganizationEditDeleteQuestionAnswerEvent): T? = null
    override fun visit(organizationEditDeleteVolunteerEvent: OrganizationEditDeleteVolunteerEvent): T? = null
    override fun visit(organizationEditDescriptionEvent: OrganizationEditDescriptionEvent): T? = null
    override fun visit(organizationEditLogoEvent: OrganizationEditLogoEvent): T? = null
    override fun visit(organizationEditNameEvent: OrganizationEditNameEvent): T? = null
    override fun visit(organizationEditTeaserImageEvent: OrganizationEditTeaserImageEvent): T? = null
    override fun visit(organizationEditUrlNameEvent: OrganizationEditUrlNameEvent): T? = null
    override fun visit(organizationEditWebsiteEvent: OrganizationEditWebsiteEvent): T? = null
    override fun visit(proposedChangeOrganizationEvent: ProposedChangeOrganizationEvent): T? = null
    override fun visit(confirmedChangeOrganizationEvent: ConfirmedChangeOrganizationEvent): T? = null
    override fun visit(organizationEditChangeGroupEvent: OrganizationEditChangeGroupEvent): T? = null
    override fun visit(organizationEditChangePictureEvent: OrganizationEditChangePictureEvent): T? = null
    override fun visit(organizationEditChangeAttendanceTimeEvent: OrganizationEditChangeAttendanceTimeEvent): T? = null
    override fun visit(organizationEditChangeAddressEvent: OrganizationEditChangeAddressEvent): T? = null
    override fun visit(organizationEditChangeQuestionAnswerEvent: OrganizationEditChangeQuestionAnswerEvent): T? = null
}