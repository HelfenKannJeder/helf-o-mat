package de.helfenkannjeder.helfomat.api.organisation.event;

/**
 * @author Valentin Zickner
 */
public interface OrganizationEventDtoVisitor<T> {

    T visit(OrganizationCreateEventDto organizationCreateEventDto);

    T visit(OrganizationDeleteEventDto organizationDeleteEventDto);

    T visit(OrganizationEditAddAddressEventDto organizationEditAddAddressEventDto);

    T visit(OrganizationEditAddAttendanceTimeEventDto organizationEditAddAttendanceTimeEventDto);

    T visit(OrganizationEditAddContactPersonEventDto organizationEditAddContactPersonEventDto);

    T visit(OrganizationEditAddGroupEventDto organizationEditAddGroupEventDto);

    T visit(OrganizationEditAddPictureEventDto organizationEditAddPictureEventDto);

    T visit(OrganizationEditAddQuestionAnswerEventDto organizationEditAddQuestionAnswerEventDto);

    T visit(OrganizationEditAddVolunteerEventDto organizationEditAddVolunteerEventDto);

    T visit(OrganizationEditDefaultAddressEventDto organizationEditDefaultAddressEventDto);

    T visit(OrganizationEditDeleteAddressEventDto organizationEditDeleteAddressEventDto);

    T visit(OrganizationEditDeleteAttendanceTimeEventDto organizationEditDeleteAttendanceTimeEventDto);

    T visit(OrganizationEditDeleteContactPersonEventDto organizationEditDeleteContactPersonEventDto);

    T visit(OrganizationEditDeleteGroupEventDto organizationEditDeleteGroupEventDto);

    T visit(OrganizationEditDeletePictureEventDto organizationEditDeletePictureEventDto);

    T visit(OrganizationEditDeleteQuestionAnswerEventDto organizationEditDeleteQuestionAnswerEventDto);

    T visit(OrganizationEditDeleteVolunteerEventDto organizationEditDeleteVolunteerEventDto);

    T visit(OrganizationEditDescriptionEventDto organizationEditDescriptionEventDto);

    T visit(OrganizationEditLogoEventDto organizationEditLogoEventDto);

    T visit(OrganizationEditNameEventDto organizationEditNameEventDto);

    T visit(OrganizationEditTeaserImageEventDto organizationEditTeaserImageEventDto);

    T visit(OrganizationEditUrlNameEventDto organizationEditUrlNameEventDto);

    T visit(OrganizationEditWebsiteEventDto organizationEditWebsiteEventDto);
}
