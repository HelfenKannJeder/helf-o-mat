package de.helfenkannjeder.helfomat.api.organisation.event;

import de.helfenkannjeder.helfomat.api.organisation.OrganisationAssembler;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationCreateEvent;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationDeleteEvent;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEditAddAddressEvent;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEditAddAttendanceTimeEvent;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEditAddContactPersonEvent;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEditAddGroupEvent;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEditAddPictureEvent;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEditAddQuestionAnswerEvent;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEditAddVolunteerEvent;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEditDefaultAddressEvent;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEditDeleteAttendanceTimeEvent;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEditDeleteContactPersonEvent;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEditDeleteGroupEvent;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEditDeletePictureEvent;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEditDeleteQuestionAnswerEvent;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEditDeleteVolunteerEvent;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEditDescriptionEvent;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEditLogoEvent;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEditNameEvent;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEditTeaserImageEvent;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEditUrlNameEvent;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEditWebsiteEvent;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEvent;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Valentin Zickner
 */
public class OrganizationEventDtoAssembler implements OrganizationEventDtoVisitor<OrganisationEvent> {

    public static List<OrganisationEvent> toOrganizationEvent(List<OrganizationEventDto> organizationEvents) {
        OrganizationEventDtoAssembler organizationEventDtoAssembler = new OrganizationEventDtoAssembler();
        return organizationEvents.stream()
            .map(organisationEvent -> organisationEvent.visit(organizationEventDtoAssembler))
            .collect(Collectors.toList());
    }

    @Override
    public OrganisationEvent visit(OrganizationCreateEventDto organizationCreateEventDto) {
        return new OrganisationCreateEvent(
            organizationCreateEventDto.getOrganisationId(),
            organizationCreateEventDto.getName(),
            organizationCreateEventDto.getUrlName(),
            organizationCreateEventDto.getOrganisationType()
        );
    }

    @Override
    public OrganisationEvent visit(OrganizationDeleteEventDto organizationDeleteEventDto) {
        return new OrganisationDeleteEvent(
            organizationDeleteEventDto.getOrganisationId()
        );
    }

    @Override
    public OrganisationEvent visit(OrganizationEditAddAddressEventDto organizationEditAddAddressEventDto) {
        return new OrganisationEditAddAddressEvent(
            organizationEditAddAddressEventDto.getOrganisationId(),
            organizationEditAddAddressEventDto.getIndex(),
            OrganisationAssembler.toAddress(organizationEditAddAddressEventDto.getAddress())
        );
    }

    @Override
    public OrganisationEvent visit(OrganizationEditAddAttendanceTimeEventDto organizationEditAddAttendanceTimeEventDto) {
        return new OrganisationEditAddAttendanceTimeEvent(
            organizationEditAddAttendanceTimeEventDto.getOrganisationId(),
            organizationEditAddAttendanceTimeEventDto.getIndex(),
            OrganisationAssembler.toAttendanceTime(organizationEditAddAttendanceTimeEventDto.getAttendanceTime())
        );
    }

    @Override
    public OrganisationEvent visit(OrganizationEditAddContactPersonEventDto organizationEditAddContactPersonEventDto) {
        return new OrganisationEditAddContactPersonEvent(
            organizationEditAddContactPersonEventDto.getOrganisationId(),
            organizationEditAddContactPersonEventDto.getIndex(),
            OrganisationAssembler.toContactPerson(organizationEditAddContactPersonEventDto.getContactPerson())
        );
    }

    @Override
    public OrganisationEvent visit(OrganizationEditAddGroupEventDto organizationEditAddGroupEventDto) {
        return new OrganisationEditAddGroupEvent(
            organizationEditAddGroupEventDto.getOrganisationId(),
            organizationEditAddGroupEventDto.getIndex(),
            OrganisationAssembler.toGroup(organizationEditAddGroupEventDto.getGroup())
        );
    }

    @Override
    public OrganisationEvent visit(OrganizationEditAddPictureEventDto organizationEditAddPictureEventDto) {
        return new OrganisationEditAddPictureEvent(
            organizationEditAddPictureEventDto.getOrganisationId(),
            organizationEditAddPictureEventDto.getIndex(),
            organizationEditAddPictureEventDto.getPictureId()
        );
    }

    @Override
    public OrganisationEvent visit(OrganizationEditAddQuestionAnswerEventDto organizationEditAddQuestionAnswerEventDto) {
        return new OrganisationEditAddQuestionAnswerEvent(
            organizationEditAddQuestionAnswerEventDto.getOrganisationId(),
            organizationEditAddQuestionAnswerEventDto.getIndex(),
            OrganisationAssembler.toQuestionAnswer(organizationEditAddQuestionAnswerEventDto.getAnsweredQuestion())
        );
    }

    @Override
    public OrganisationEvent visit(OrganizationEditAddVolunteerEventDto organizationEditAddVolunteerEventDto) {
        return new OrganisationEditAddVolunteerEvent(
            organizationEditAddVolunteerEventDto.getOrganisationId(),
            organizationEditAddVolunteerEventDto.getIndex(),
            OrganisationAssembler.toVolunteer(organizationEditAddVolunteerEventDto.getVolunteer())
        );
    }

    @Override
    public OrganisationEvent visit(OrganizationEditDefaultAddressEventDto organizationEditDefaultAddressEventDto) {
        return new OrganisationEditDefaultAddressEvent(
            organizationEditDefaultAddressEventDto.getOrganisationId(),
            OrganisationAssembler.toAddress(organizationEditDefaultAddressEventDto.getAddress())
        );
    }

    @Override
    public OrganisationEvent visit(OrganizationEditDeleteAddressEventDto organizationEditDeleteAddressEventDto) {
        return new OrganisationEditDefaultAddressEvent(
            organizationEditDeleteAddressEventDto.getOrganisationId(),
            OrganisationAssembler.toAddress(organizationEditDeleteAddressEventDto.getAddress())
        );
    }

    @Override
    public OrganisationEvent visit(OrganizationEditDeleteAttendanceTimeEventDto organizationEditDeleteAttendanceTimeEventDto) {
        return new OrganisationEditDeleteAttendanceTimeEvent(
            organizationEditDeleteAttendanceTimeEventDto.getOrganisationId(),
            OrganisationAssembler.toAttendanceTime(organizationEditDeleteAttendanceTimeEventDto.getAttendanceTime())
        );
    }

    @Override
    public OrganisationEvent visit(OrganizationEditDeleteContactPersonEventDto organizationEditDeleteContactPersonEventDto) {
        return new OrganisationEditDeleteContactPersonEvent(
            organizationEditDeleteContactPersonEventDto.getOrganisationId(),
            OrganisationAssembler.toContactPerson(organizationEditDeleteContactPersonEventDto.getContactPerson())
        );
    }

    @Override
    public OrganisationEvent visit(OrganizationEditDeleteGroupEventDto organizationEditDeleteGroupEventDto) {
        return new OrganisationEditDeleteGroupEvent(
            organizationEditDeleteGroupEventDto.getOrganisationId(),
            OrganisationAssembler.toGroup(organizationEditDeleteGroupEventDto.getGroup())
        );
    }

    @Override
    public OrganisationEvent visit(OrganizationEditDeletePictureEventDto organizationEditDeletePictureEventDto) {
        return new OrganisationEditDeletePictureEvent(
            organizationEditDeletePictureEventDto.getOrganisationId(),
            organizationEditDeletePictureEventDto.getPictureId()
        );
    }

    @Override
    public OrganisationEvent visit(OrganizationEditDeleteQuestionAnswerEventDto organizationEditDeleteQuestionAnswerEventDto) {
        return new OrganisationEditDeleteQuestionAnswerEvent(
            organizationEditDeleteQuestionAnswerEventDto.getOrganisationId(),
            OrganisationAssembler.toQuestionAnswer(organizationEditDeleteQuestionAnswerEventDto.getAnsweredQuestion())
        );
    }

    @Override
    public OrganisationEvent visit(OrganizationEditDeleteVolunteerEventDto organizationEditDeleteVolunteerEventDto) {
        return new OrganisationEditDeleteVolunteerEvent(
            organizationEditDeleteVolunteerEventDto.getOrganisationId(),
            OrganisationAssembler.toVolunteer(organizationEditDeleteVolunteerEventDto.getVolunteer())
        );
    }

    @Override
    public OrganisationEvent visit(OrganizationEditDescriptionEventDto organizationEditDescriptionEventDto) {
        return new OrganisationEditDescriptionEvent(
            organizationEditDescriptionEventDto.getOrganisationId(),
            organizationEditDescriptionEventDto.getDescription()
        );
    }

    @Override
    public OrganisationEvent visit(OrganizationEditLogoEventDto organizationEditLogoEventDto) {
        return new OrganisationEditLogoEvent(
            organizationEditLogoEventDto.getOrganisationId(),
            organizationEditLogoEventDto.getLogo()
        );
    }

    @Override
    public OrganisationEvent visit(OrganizationEditNameEventDto organizationEditNameEventDto) {
        return new OrganisationEditNameEvent(
            organizationEditNameEventDto.getOrganisationId(),
            organizationEditNameEventDto.getName()
        );
    }

    @Override
    public OrganisationEvent visit(OrganizationEditTeaserImageEventDto organizationEditTeaserImageEventDto) {
        return new OrganisationEditTeaserImageEvent(
            organizationEditTeaserImageEventDto.getOrganisationId(),
            organizationEditTeaserImageEventDto.getTeaserImage()
        );
    }

    @Override
    public OrganisationEvent visit(OrganizationEditUrlNameEventDto organizationEditUrlNameEventDto) {
        return new OrganisationEditUrlNameEvent(
            organizationEditUrlNameEventDto.getOrganisationId(),
            organizationEditUrlNameEventDto.getUrlName()
        );
    }

    @Override
    public OrganisationEvent visit(OrganizationEditWebsiteEventDto organizationEditWebsiteEventDto) {
        return new OrganisationEditWebsiteEvent(
            organizationEditWebsiteEventDto.getOrganisationId(),
            organizationEditWebsiteEventDto.getWebsite()
        );
    }
}
