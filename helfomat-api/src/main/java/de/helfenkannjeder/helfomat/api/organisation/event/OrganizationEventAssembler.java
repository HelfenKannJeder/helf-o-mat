package de.helfenkannjeder.helfomat.api.organisation.event;

import de.helfenkannjeder.helfomat.api.organisation.OrganisationAssembler;
import de.helfenkannjeder.helfomat.core.organisation.OrganizationEventVisitor;
import de.helfenkannjeder.helfomat.core.organisation.event.ConfirmedChangeOrganizationEvent;
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
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEditDeleteAddressEvent;
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
import de.helfenkannjeder.helfomat.core.organisation.event.ProposedChangeOrganizationEvent;
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

    public static List<OrganizationEventDto> toOrganizationEventDto(List<OrganisationEvent> organizationEvents, List<Question> questions) {
        OrganizationEventAssembler organizationEventAssembler = new OrganizationEventAssembler(questions);
        return organizationEvents.stream()
            .map(organisationEvent -> organisationEvent.visit(organizationEventAssembler))
            .collect(Collectors.toList());
    }

    @Override
    public OrganizationEventDto visit(OrganisationCreateEvent organisationCreateEvent) {
        return new OrganizationCreateEventDto(
            organisationCreateEvent.getOrganisationId(),
            organisationCreateEvent.getName(),
            organisationCreateEvent.getUrlName(),
            organisationCreateEvent.getOrganisationType()
        );
    }

    @Override
    public OrganizationEventDto visit(OrganisationDeleteEvent organisationDeleteEvent) {
        return new OrganizationDeleteEventDto(organisationDeleteEvent.getOrganisationId());
    }

    @Override
    public OrganizationEventDto visit(OrganisationEditAddAddressEvent organisationEditAddAddressEvent) {
        return new OrganizationEditAddAddressEventDto(
            organisationEditAddAddressEvent.getOrganisationId(),
            organisationEditAddAddressEvent.getIndex(),
            OrganisationAssembler.toAddressDto(organisationEditAddAddressEvent.getAddress())
        );
    }

    @Override
    public OrganizationEventDto visit(OrganisationEditAddAttendanceTimeEvent organisationEditAddAttendanceTimeEvent) {
        return new OrganizationEditAddAttendanceTimeEventDto(
            organisationEditAddAttendanceTimeEvent.getOrganisationId(),
            organisationEditAddAttendanceTimeEvent.getIndex(),
            OrganisationAssembler.toAttendanceTimeDto(organisationEditAddAttendanceTimeEvent.getAttendanceTime())
        );
    }

    @Override
    public OrganizationEventDto visit(OrganisationEditAddContactPersonEvent organisationEditAddContactPersonEvent) {
        return new OrganizationEditAddContactPersonEventDto(
            organisationEditAddContactPersonEvent.getOrganisationId(),
            organisationEditAddContactPersonEvent.getIndex(),
            OrganisationAssembler.toContactPersonDto(organisationEditAddContactPersonEvent.getContactPerson())
        );
    }

    @Override
    public OrganizationEventDto visit(OrganisationEditAddGroupEvent organisationEditAddGroupEvent) {
        return new OrganizationEditAddGroupEventDto(
            organisationEditAddGroupEvent.getOrganisationId(),
            organisationEditAddGroupEvent.getIndex(),
            OrganisationAssembler.toGroupDto(organisationEditAddGroupEvent.getGroup())
        );
    }

    @Override
    public OrganizationEventDto visit(OrganisationEditAddPictureEvent organisationEditAddPictureEvent) {
        return new OrganizationEditAddPictureEventDto(
            organisationEditAddPictureEvent.getOrganisationId(),
            organisationEditAddPictureEvent.getIndex(),
            organisationEditAddPictureEvent.getPictureId()
        );
    }

    @Override
    public OrganizationEventDto visit(OrganisationEditAddQuestionAnswerEvent organisationEditAddQuestionAnswerEvent) {
        return new OrganizationEditAddQuestionAnswerEventDto(
            organisationEditAddQuestionAnswerEvent.getOrganisationId(),
            organisationEditAddQuestionAnswerEvent.getIndex(),
            OrganisationAssembler.toAnsweredQuestionDto(
                organisationEditAddQuestionAnswerEvent.getQuestionAnswer(),
                OrganisationAssembler.determineQuestionText(questions, organisationEditAddQuestionAnswerEvent.getQuestionAnswer().getQuestionId())
            )
        );
    }

    @Override
    public OrganizationEventDto visit(OrganisationEditAddVolunteerEvent organisationEditAddVolunteerEvent) {
        return new OrganizationEditAddVolunteerEventDto(
            organisationEditAddVolunteerEvent.getOrganisationId(),
            organisationEditAddVolunteerEvent.getIndex(),
            OrganisationAssembler.toVolunteerDto(organisationEditAddVolunteerEvent.getVolunteer())
        );
    }

    @Override
    public OrganizationEventDto visit(OrganisationEditDefaultAddressEvent organisationEditDefaultAddressEvent) {
        return new OrganizationEditDefaultAddressEventDto(
            organisationEditDefaultAddressEvent.getOrganisationId(),
            OrganisationAssembler.toAddressDto(organisationEditDefaultAddressEvent.getDefaultAddress())
        );
    }

    @Override
    public OrganizationEventDto visit(OrganisationEditDeleteAddressEvent organisationEditDeleteAddressEvent) {
        return new OrganizationEditDeleteAddressEventDto(
            organisationEditDeleteAddressEvent.getOrganisationId(),
            OrganisationAssembler.toAddressDto(organisationEditDeleteAddressEvent.getAddress())
        );
    }

    @Override
    public OrganizationEventDto visit(OrganisationEditDeleteAttendanceTimeEvent organisationEditDeleteAttendanceTimeEvent) {
        return new OrganizationEditDeleteAttendanceTimeEventDto(
            organisationEditDeleteAttendanceTimeEvent.getOrganisationId(),
            OrganisationAssembler.toAttendanceTimeDto(organisationEditDeleteAttendanceTimeEvent.getAttendanceTime())
        );
    }

    @Override
    public OrganizationEventDto visit(OrganisationEditDeleteContactPersonEvent organisationEditDeleteContactPersonEvent) {
        return new OrganizationEditDeleteContactPersonEventDto(
            organisationEditDeleteContactPersonEvent.getOrganisationId(),
            OrganisationAssembler.toContactPersonDto(organisationEditDeleteContactPersonEvent.getContactPerson())
        );
    }

    @Override
    public OrganizationEventDto visit(OrganisationEditDeleteGroupEvent organisationEditDeleteGroupEvent) {
        return new OrganizationEditDeleteGroupEventDto(
            organisationEditDeleteGroupEvent.getOrganisationId(),
            OrganisationAssembler.toGroupDto(organisationEditDeleteGroupEvent.getGroup())
        );
    }

    @Override
    public OrganizationEventDto visit(OrganisationEditDeletePictureEvent organisationEditDeletePictureEvent) {
        return new OrganizationEditDeletePictureEventDto(
            organisationEditDeletePictureEvent.getOrganisationId(),
            organisationEditDeletePictureEvent.getPictureId()
        );
    }

    @Override
    public OrganizationEventDto visit(OrganisationEditDeleteQuestionAnswerEvent organisationEditDeleteQuestionAnswerEvent) {
        return new OrganizationEditDeleteQuestionAnswerEventDto(
            organisationEditDeleteQuestionAnswerEvent.getOrganisationId(),
            OrganisationAssembler.toAnsweredQuestionDto(
                organisationEditDeleteQuestionAnswerEvent.getQuestionAnswer(),
                OrganisationAssembler.determineQuestionText(questions, organisationEditDeleteQuestionAnswerEvent.getQuestionAnswer().getQuestionId())
            )
        );
    }

    @Override
    public OrganizationEventDto visit(OrganisationEditDeleteVolunteerEvent organisationEditDeleteVolunteerEvent) {
        return new OrganizationEditDeleteVolunteerEventDto(
            organisationEditDeleteVolunteerEvent.getOrganisationId(),
            OrganisationAssembler.toVolunteerDto(organisationEditDeleteVolunteerEvent.getVolunteer())
        );
    }

    @Override
    public OrganizationEventDto visit(OrganisationEditDescriptionEvent organisationEditDescriptionEvent) {
        return new OrganizationEditDescriptionEventDto(
            organisationEditDescriptionEvent.getOrganisationId(),
            organisationEditDescriptionEvent.getDescription()
        );
    }

    @Override
    public OrganizationEventDto visit(OrganisationEditLogoEvent organisationEditLogoEvent) {
        return new OrganizationEditLogoEventDto(
            organisationEditLogoEvent.getOrganisationId(),
            organisationEditLogoEvent.getLogo()
        );
    }

    @Override
    public OrganizationEventDto visit(OrganisationEditNameEvent organisationEditNameEvent) {
        return new OrganizationEditNameEventDto(
            organisationEditNameEvent.getOrganisationId(),
            organisationEditNameEvent.getName()
        );
    }

    @Override
    public OrganizationEventDto visit(OrganisationEditTeaserImageEvent organisationEditTeaserImageEvent) {
        return new OrganizationEditTeaserImageEventDto(
            organisationEditTeaserImageEvent.getOrganisationId(),
            organisationEditTeaserImageEvent.getTeaserImage()
        );
    }

    @Override
    public OrganizationEventDto visit(OrganisationEditUrlNameEvent organisationEditUrlNameEvent) {
        return new OrganizationEditUrlNameEventDto(
            organisationEditUrlNameEvent.getOrganisationId(),
            organisationEditUrlNameEvent.getUrlName()
        );
    }

    @Override
    public OrganizationEventDto visit(OrganisationEditWebsiteEvent organisationEditWebsiteEvent) {
        return new OrganizationEditWebsiteEventDto(
            organisationEditWebsiteEvent.getOrganisationId(),
            organisationEditWebsiteEvent.getWebsite()
        );
    }

    @Override
    public OrganizationEventDto visit(ProposedChangeOrganizationEvent proposedChangeOrganizationEvent) {
        return new ProposedChangeOrganizationEventDto(
            proposedChangeOrganizationEvent.getOrganisationId(),
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
            confirmedChangeOrganizationEvent.getOrganisationId(),
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
