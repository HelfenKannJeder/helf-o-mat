package de.helfenkannjeder.helfomat.api.organization.event;

import de.helfenkannjeder.helfomat.api.organization.OrganizationAssembler;
import de.helfenkannjeder.helfomat.core.organization.event.ConfirmedChangeOrganizationEvent;
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationCreateEvent;
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationDeleteEvent;
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEditAddAddressEvent;
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEditAddAttendanceTimeEvent;
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEditAddContactPersonEvent;
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEditAddGroupEvent;
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEditAddPictureEvent;
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEditAddQuestionAnswerEvent;
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEditAddVolunteerEvent;
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEditDefaultAddressEvent;
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEditDeleteAttendanceTimeEvent;
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEditDeleteContactPersonEvent;
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEditDeleteGroupEvent;
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEditDeletePictureEvent;
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEditDeleteQuestionAnswerEvent;
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEditDeleteVolunteerEvent;
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEditDescriptionEvent;
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEditLogoEvent;
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEditNameEvent;
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEditTeaserImageEvent;
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEditUrlNameEvent;
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEditWebsiteEvent;
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEvent;
import de.helfenkannjeder.helfomat.core.organization.event.ProposedChangeOrganizationEvent;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Valentin Zickner
 */
public class OrganizationEventDtoAssembler implements OrganizationEventDtoVisitor<OrganizationEvent> {

    public static List<OrganizationEvent> toOrganizationEvent(List<OrganizationEventDto> organizationEvents) {
        OrganizationEventDtoAssembler organizationEventDtoAssembler = new OrganizationEventDtoAssembler();
        return organizationEvents.stream()
            .map(organizationEvent -> organizationEvent.visit(organizationEventDtoAssembler))
            .collect(Collectors.toList());
    }

    @Override
    public OrganizationEvent visit(OrganizationCreateEventDto organizationCreateEventDto) {
        return new OrganizationCreateEvent(
            organizationCreateEventDto.getOrganizationId(),
            organizationCreateEventDto.getName(),
            organizationCreateEventDto.getUrlName(),
            organizationCreateEventDto.getOrganizationType()
        );
    }

    @Override
    public OrganizationEvent visit(OrganizationDeleteEventDto organizationDeleteEventDto) {
        return new OrganizationDeleteEvent(
            organizationDeleteEventDto.getOrganizationId()
        );
    }

    @Override
    public OrganizationEvent visit(OrganizationEditAddAddressEventDto organizationEditAddAddressEventDto) {
        return new OrganizationEditAddAddressEvent(
            organizationEditAddAddressEventDto.getOrganizationId(),
            organizationEditAddAddressEventDto.getIndex(),
            OrganizationAssembler.toAddress(organizationEditAddAddressEventDto.getAddress())
        );
    }

    @Override
    public OrganizationEvent visit(OrganizationEditAddAttendanceTimeEventDto organizationEditAddAttendanceTimeEventDto) {
        return new OrganizationEditAddAttendanceTimeEvent(
            organizationEditAddAttendanceTimeEventDto.getOrganizationId(),
            organizationEditAddAttendanceTimeEventDto.getIndex(),
            OrganizationAssembler.toAttendanceTime(organizationEditAddAttendanceTimeEventDto.getAttendanceTime())
        );
    }

    @Override
    public OrganizationEvent visit(OrganizationEditAddContactPersonEventDto organizationEditAddContactPersonEventDto) {
        return new OrganizationEditAddContactPersonEvent(
            organizationEditAddContactPersonEventDto.getOrganizationId(),
            organizationEditAddContactPersonEventDto.getIndex(),
            OrganizationAssembler.toContactPerson(organizationEditAddContactPersonEventDto.getContactPerson())
        );
    }

    @Override
    public OrganizationEvent visit(OrganizationEditAddGroupEventDto organizationEditAddGroupEventDto) {
        return new OrganizationEditAddGroupEvent(
            organizationEditAddGroupEventDto.getOrganizationId(),
            organizationEditAddGroupEventDto.getIndex(),
            OrganizationAssembler.toGroup(organizationEditAddGroupEventDto.getGroup())
        );
    }

    @Override
    public OrganizationEvent visit(OrganizationEditAddPictureEventDto organizationEditAddPictureEventDto) {
        return new OrganizationEditAddPictureEvent(
            organizationEditAddPictureEventDto.getOrganizationId(),
            organizationEditAddPictureEventDto.getIndex(),
            organizationEditAddPictureEventDto.getPictureId()
        );
    }

    @Override
    public OrganizationEvent visit(OrganizationEditAddQuestionAnswerEventDto organizationEditAddQuestionAnswerEventDto) {
        return new OrganizationEditAddQuestionAnswerEvent(
            organizationEditAddQuestionAnswerEventDto.getOrganizationId(),
            organizationEditAddQuestionAnswerEventDto.getIndex(),
            OrganizationAssembler.toQuestionAnswer(organizationEditAddQuestionAnswerEventDto.getAnsweredQuestion())
        );
    }

    @Override
    public OrganizationEvent visit(OrganizationEditAddVolunteerEventDto organizationEditAddVolunteerEventDto) {
        return new OrganizationEditAddVolunteerEvent(
            organizationEditAddVolunteerEventDto.getOrganizationId(),
            organizationEditAddVolunteerEventDto.getIndex(),
            OrganizationAssembler.toVolunteer(organizationEditAddVolunteerEventDto.getVolunteer())
        );
    }

    @Override
    public OrganizationEvent visit(OrganizationEditDefaultAddressEventDto organizationEditDefaultAddressEventDto) {
        return new OrganizationEditDefaultAddressEvent(
            organizationEditDefaultAddressEventDto.getOrganizationId(),
            OrganizationAssembler.toAddress(organizationEditDefaultAddressEventDto.getAddress())
        );
    }

    @Override
    public OrganizationEvent visit(OrganizationEditDeleteAddressEventDto organizationEditDeleteAddressEventDto) {
        return new OrganizationEditDefaultAddressEvent(
            organizationEditDeleteAddressEventDto.getOrganizationId(),
            OrganizationAssembler.toAddress(organizationEditDeleteAddressEventDto.getAddress())
        );
    }

    @Override
    public OrganizationEvent visit(OrganizationEditDeleteAttendanceTimeEventDto organizationEditDeleteAttendanceTimeEventDto) {
        return new OrganizationEditDeleteAttendanceTimeEvent(
            organizationEditDeleteAttendanceTimeEventDto.getOrganizationId(),
            OrganizationAssembler.toAttendanceTime(organizationEditDeleteAttendanceTimeEventDto.getAttendanceTime())
        );
    }

    @Override
    public OrganizationEvent visit(OrganizationEditDeleteContactPersonEventDto organizationEditDeleteContactPersonEventDto) {
        return new OrganizationEditDeleteContactPersonEvent(
            organizationEditDeleteContactPersonEventDto.getOrganizationId(),
            OrganizationAssembler.toContactPerson(organizationEditDeleteContactPersonEventDto.getContactPerson())
        );
    }

    @Override
    public OrganizationEvent visit(OrganizationEditDeleteGroupEventDto organizationEditDeleteGroupEventDto) {
        return new OrganizationEditDeleteGroupEvent(
            organizationEditDeleteGroupEventDto.getOrganizationId(),
            OrganizationAssembler.toGroup(organizationEditDeleteGroupEventDto.getGroup())
        );
    }

    @Override
    public OrganizationEvent visit(OrganizationEditDeletePictureEventDto organizationEditDeletePictureEventDto) {
        return new OrganizationEditDeletePictureEvent(
            organizationEditDeletePictureEventDto.getOrganizationId(),
            organizationEditDeletePictureEventDto.getPictureId()
        );
    }

    @Override
    public OrganizationEvent visit(OrganizationEditDeleteQuestionAnswerEventDto organizationEditDeleteQuestionAnswerEventDto) {
        return new OrganizationEditDeleteQuestionAnswerEvent(
            organizationEditDeleteQuestionAnswerEventDto.getOrganizationId(),
            OrganizationAssembler.toQuestionAnswer(organizationEditDeleteQuestionAnswerEventDto.getAnsweredQuestion())
        );
    }

    @Override
    public OrganizationEvent visit(OrganizationEditDeleteVolunteerEventDto organizationEditDeleteVolunteerEventDto) {
        return new OrganizationEditDeleteVolunteerEvent(
            organizationEditDeleteVolunteerEventDto.getOrganizationId(),
            OrganizationAssembler.toVolunteer(organizationEditDeleteVolunteerEventDto.getVolunteer())
        );
    }

    @Override
    public OrganizationEvent visit(OrganizationEditDescriptionEventDto organizationEditDescriptionEventDto) {
        return new OrganizationEditDescriptionEvent(
            organizationEditDescriptionEventDto.getOrganizationId(),
            organizationEditDescriptionEventDto.getDescription()
        );
    }

    @Override
    public OrganizationEvent visit(OrganizationEditLogoEventDto organizationEditLogoEventDto) {
        return new OrganizationEditLogoEvent(
            organizationEditLogoEventDto.getOrganizationId(),
            organizationEditLogoEventDto.getLogo()
        );
    }

    @Override
    public OrganizationEvent visit(OrganizationEditNameEventDto organizationEditNameEventDto) {
        return new OrganizationEditNameEvent(
            organizationEditNameEventDto.getOrganizationId(),
            organizationEditNameEventDto.getName()
        );
    }

    @Override
    public OrganizationEvent visit(OrganizationEditTeaserImageEventDto organizationEditTeaserImageEventDto) {
        return new OrganizationEditTeaserImageEvent(
            organizationEditTeaserImageEventDto.getOrganizationId(),
            organizationEditTeaserImageEventDto.getTeaserImage()
        );
    }

    @Override
    public OrganizationEvent visit(OrganizationEditUrlNameEventDto organizationEditUrlNameEventDto) {
        return new OrganizationEditUrlNameEvent(
            organizationEditUrlNameEventDto.getOrganizationId(),
            organizationEditUrlNameEventDto.getUrlName()
        );
    }

    @Override
    public OrganizationEvent visit(OrganizationEditWebsiteEventDto organizationEditWebsiteEventDto) {
        return new OrganizationEditWebsiteEvent(
            organizationEditWebsiteEventDto.getOrganizationId(),
            organizationEditWebsiteEventDto.getWebsite()
        );
    }

    @Override
    public OrganizationEvent visit(ProposedChangeOrganizationEventDto proposedChangeOrganizationEventDto) {
        return new ProposedChangeOrganizationEvent(
            proposedChangeOrganizationEventDto.getOrganizationId(),
            proposedChangeOrganizationEventDto.getAuthor(),
            proposedChangeOrganizationEventDto.getSources(),
            proposedChangeOrganizationEventDto.getChanges()
                .stream()
                .map(organizationEventDto -> organizationEventDto.visit(this))
                .collect(Collectors.toList())
        );
    }

    @Override
    public OrganizationEvent visit(ConfirmedChangeOrganizationEventDto confirmedChangeOrganizationEventDto) {
        return new ConfirmedChangeOrganizationEvent(
            confirmedChangeOrganizationEventDto.getOrganizationId(),
            confirmedChangeOrganizationEventDto.getApprovedBy(),
            confirmedChangeOrganizationEventDto.getAuthor(),
            confirmedChangeOrganizationEventDto.getSources(),
            confirmedChangeOrganizationEventDto.getChanges()
                .stream()
                .map(organizationEventDto -> organizationEventDto.visit(this))
                .collect(Collectors.toList())
        );
    }

}
