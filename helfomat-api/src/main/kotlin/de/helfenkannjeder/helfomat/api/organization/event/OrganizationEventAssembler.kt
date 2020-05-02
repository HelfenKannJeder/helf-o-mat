package de.helfenkannjeder.helfomat.api.organization.event;

import de.helfenkannjeder.helfomat.api.organization.OrganizationAssembler;
import de.helfenkannjeder.helfomat.core.organization.OrganizationEventVisitor;
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
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEditDeleteAddressEvent;
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
import de.helfenkannjeder.helfomat.core.question.Question;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Valentin Zickner
 */
public class OrganizationEventAssembler implements OrganizationEventVisitor<OrganizationEventDto> {

    private final List<Question> questions;

    public OrganizationEventAssembler(List<Question> questions) {
        this.questions = questions;
    }

    public static List<OrganizationEventDto> toOrganizationEventDto(List<OrganizationEvent> organizationEvents, List<Question> questions) {
        OrganizationEventAssembler organizationEventAssembler = new OrganizationEventAssembler(questions);
        return organizationEvents.stream()
            .map(organizationEvent -> organizationEvent.visit(organizationEventAssembler))
            .collect(Collectors.toList());
    }

    @Override
    public OrganizationEventDto visit(OrganizationCreateEvent organizationCreateEvent) {
        return new OrganizationCreateEventDto(
            organizationCreateEvent.getOrganizationId(),
            organizationCreateEvent.getName(),
            organizationCreateEvent.getUrlName(),
            organizationCreateEvent.getOrganizationType()
        );
    }

    @Override
    public OrganizationEventDto visit(OrganizationDeleteEvent organizationDeleteEvent) {
        return new OrganizationDeleteEventDto(organizationDeleteEvent.getOrganizationId());
    }

    @Override
    public OrganizationEventDto visit(OrganizationEditAddAddressEvent organizationEditAddAddressEvent) {
        return new OrganizationEditAddAddressEventDto(
            organizationEditAddAddressEvent.getOrganizationId(),
            organizationEditAddAddressEvent.getIndex(),
            OrganizationAssembler.toAddressDto(organizationEditAddAddressEvent.getAddress())
        );
    }

    @Override
    public OrganizationEventDto visit(OrganizationEditAddAttendanceTimeEvent organizationEditAddAttendanceTimeEvent) {
        return new OrganizationEditAddAttendanceTimeEventDto(
            organizationEditAddAttendanceTimeEvent.getOrganizationId(),
            organizationEditAddAttendanceTimeEvent.getIndex(),
            OrganizationAssembler.toAttendanceTimeDto(organizationEditAddAttendanceTimeEvent.getAttendanceTime())
        );
    }

    @Override
    public OrganizationEventDto visit(OrganizationEditAddContactPersonEvent organizationEditAddContactPersonEvent) {
        return new OrganizationEditAddContactPersonEventDto(
            organizationEditAddContactPersonEvent.getOrganizationId(),
            organizationEditAddContactPersonEvent.getIndex(),
            OrganizationAssembler.toContactPersonDto(organizationEditAddContactPersonEvent.getContactPerson())
        );
    }

    @Override
    public OrganizationEventDto visit(OrganizationEditAddGroupEvent organizationEditAddGroupEvent) {
        return new OrganizationEditAddGroupEventDto(
            organizationEditAddGroupEvent.getOrganizationId(),
            organizationEditAddGroupEvent.getIndex(),
            OrganizationAssembler.toGroupDto(organizationEditAddGroupEvent.getGroup())
        );
    }

    @Override
    public OrganizationEventDto visit(OrganizationEditAddPictureEvent organizationEditAddPictureEvent) {
        return new OrganizationEditAddPictureEventDto(
            organizationEditAddPictureEvent.getOrganizationId(),
            organizationEditAddPictureEvent.getIndex(),
            organizationEditAddPictureEvent.getPictureId()
        );
    }

    @Override
    public OrganizationEventDto visit(OrganizationEditAddQuestionAnswerEvent organizationEditAddQuestionAnswerEvent) {
        return new OrganizationEditAddQuestionAnswerEventDto(
            organizationEditAddQuestionAnswerEvent.getOrganizationId(),
            organizationEditAddQuestionAnswerEvent.getIndex(),
            OrganizationAssembler.toAnsweredQuestionDto(
                organizationEditAddQuestionAnswerEvent.getQuestionAnswer(),
                OrganizationAssembler.determineQuestionText(questions, organizationEditAddQuestionAnswerEvent.getQuestionAnswer().getQuestionId())
            )
        );
    }

    @Override
    public OrganizationEventDto visit(OrganizationEditAddVolunteerEvent organizationEditAddVolunteerEvent) {
        return new OrganizationEditAddVolunteerEventDto(
            organizationEditAddVolunteerEvent.getOrganizationId(),
            organizationEditAddVolunteerEvent.getIndex(),
            OrganizationAssembler.toVolunteerDto(organizationEditAddVolunteerEvent.getVolunteer())
        );
    }

    @Override
    public OrganizationEventDto visit(OrganizationEditDefaultAddressEvent organizationEditDefaultAddressEvent) {
        return new OrganizationEditDefaultAddressEventDto(
            organizationEditDefaultAddressEvent.getOrganizationId(),
            OrganizationAssembler.toAddressDto(organizationEditDefaultAddressEvent.getDefaultAddress())
        );
    }

    @Override
    public OrganizationEventDto visit(OrganizationEditDeleteAddressEvent organizationEditDeleteAddressEvent) {
        return new OrganizationEditDeleteAddressEventDto(
            organizationEditDeleteAddressEvent.getOrganizationId(),
            OrganizationAssembler.toAddressDto(organizationEditDeleteAddressEvent.getAddress())
        );
    }

    @Override
    public OrganizationEventDto visit(OrganizationEditDeleteAttendanceTimeEvent organizationEditDeleteAttendanceTimeEvent) {
        return new OrganizationEditDeleteAttendanceTimeEventDto(
            organizationEditDeleteAttendanceTimeEvent.getOrganizationId(),
            OrganizationAssembler.toAttendanceTimeDto(organizationEditDeleteAttendanceTimeEvent.getAttendanceTime())
        );
    }

    @Override
    public OrganizationEventDto visit(OrganizationEditDeleteContactPersonEvent organizationEditDeleteContactPersonEvent) {
        return new OrganizationEditDeleteContactPersonEventDto(
            organizationEditDeleteContactPersonEvent.getOrganizationId(),
            OrganizationAssembler.toContactPersonDto(organizationEditDeleteContactPersonEvent.getContactPerson())
        );
    }

    @Override
    public OrganizationEventDto visit(OrganizationEditDeleteGroupEvent organizationEditDeleteGroupEvent) {
        return new OrganizationEditDeleteGroupEventDto(
            organizationEditDeleteGroupEvent.getOrganizationId(),
            OrganizationAssembler.toGroupDto(organizationEditDeleteGroupEvent.getGroup())
        );
    }

    @Override
    public OrganizationEventDto visit(OrganizationEditDeletePictureEvent organizationEditDeletePictureEvent) {
        return new OrganizationEditDeletePictureEventDto(
            organizationEditDeletePictureEvent.getOrganizationId(),
            organizationEditDeletePictureEvent.getPictureId()
        );
    }

    @Override
    public OrganizationEventDto visit(OrganizationEditDeleteQuestionAnswerEvent organizationEditDeleteQuestionAnswerEvent) {
        return new OrganizationEditDeleteQuestionAnswerEventDto(
            organizationEditDeleteQuestionAnswerEvent.getOrganizationId(),
            OrganizationAssembler.toAnsweredQuestionDto(
                organizationEditDeleteQuestionAnswerEvent.getQuestionAnswer(),
                OrganizationAssembler.determineQuestionText(questions, organizationEditDeleteQuestionAnswerEvent.getQuestionAnswer().getQuestionId())
            )
        );
    }

    @Override
    public OrganizationEventDto visit(OrganizationEditDeleteVolunteerEvent organizationEditDeleteVolunteerEvent) {
        return new OrganizationEditDeleteVolunteerEventDto(
            organizationEditDeleteVolunteerEvent.getOrganizationId(),
            OrganizationAssembler.toVolunteerDto(organizationEditDeleteVolunteerEvent.getVolunteer())
        );
    }

    @Override
    public OrganizationEventDto visit(OrganizationEditDescriptionEvent organizationEditDescriptionEvent) {
        return new OrganizationEditDescriptionEventDto(
            organizationEditDescriptionEvent.getOrganizationId(),
            organizationEditDescriptionEvent.getDescription()
        );
    }

    @Override
    public OrganizationEventDto visit(OrganizationEditLogoEvent organizationEditLogoEvent) {
        return new OrganizationEditLogoEventDto(
            organizationEditLogoEvent.getOrganizationId(),
            organizationEditLogoEvent.getLogo()
        );
    }

    @Override
    public OrganizationEventDto visit(OrganizationEditNameEvent organizationEditNameEvent) {
        return new OrganizationEditNameEventDto(
            organizationEditNameEvent.getOrganizationId(),
            organizationEditNameEvent.getName()
        );
    }

    @Override
    public OrganizationEventDto visit(OrganizationEditTeaserImageEvent organizationEditTeaserImageEvent) {
        return new OrganizationEditTeaserImageEventDto(
            organizationEditTeaserImageEvent.getOrganizationId(),
            organizationEditTeaserImageEvent.getTeaserImage()
        );
    }

    @Override
    public OrganizationEventDto visit(OrganizationEditUrlNameEvent organizationEditUrlNameEvent) {
        return new OrganizationEditUrlNameEventDto(
            organizationEditUrlNameEvent.getOrganizationId(),
            organizationEditUrlNameEvent.getUrlName()
        );
    }

    @Override
    public OrganizationEventDto visit(OrganizationEditWebsiteEvent organizationEditWebsiteEvent) {
        return new OrganizationEditWebsiteEventDto(
            organizationEditWebsiteEvent.getOrganizationId(),
            organizationEditWebsiteEvent.getWebsite()
        );
    }

    @Override
    public OrganizationEventDto visit(ProposedChangeOrganizationEvent proposedChangeOrganizationEvent) {
        return new ProposedChangeOrganizationEventDto(
            proposedChangeOrganizationEvent.getOrganizationId(),
            proposedChangeOrganizationEvent.getAuthor(),
            proposedChangeOrganizationEvent.getSources(),
            proposedChangeOrganizationEvent.getChanges()
                .stream()
                .map(organizationEvent -> organizationEvent.visit(this))
                .collect(Collectors.toList())
        );
    }

    @Override
    public OrganizationEventDto visit(ConfirmedChangeOrganizationEvent confirmedChangeOrganizationEvent) {
        return new ConfirmedChangeOrganizationEventDto(
            confirmedChangeOrganizationEvent.getOrganizationId(),
            confirmedChangeOrganizationEvent.getApprovedBy(),
            confirmedChangeOrganizationEvent.getAuthor(),
            confirmedChangeOrganizationEvent.getSources(),
            confirmedChangeOrganizationEvent.getChanges()
                .stream()
                .map(organizationEvent -> organizationEvent.visit(this))
                .collect(Collectors.toList())
        );
    }
}
