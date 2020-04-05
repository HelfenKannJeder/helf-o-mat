package de.helfenkannjeder.helfomat.core.organisation;

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
import de.helfenkannjeder.helfomat.core.organisation.event.ProposedChangeOrganizationEvent;

public interface OrganizationEventVisitor<T> {

    T visit(OrganisationCreateEvent organisationCreateEvent);

    T visit(OrganisationDeleteEvent organisationDeleteEvent);

    T visit(OrganisationEditAddAddressEvent organisationEditAddAddressEvent);

    T visit(OrganisationEditAddAttendanceTimeEvent organisationEditAddAttendanceTimeEvent);

    T visit(OrganisationEditAddContactPersonEvent organisationEditAddContactPersonEvent);

    T visit(OrganisationEditAddGroupEvent organisationEditAddGroupEvent);

    T visit(OrganisationEditAddPictureEvent organisationEditAddPictureEvent);

    T visit(OrganisationEditAddQuestionAnswerEvent organisationEditAddQuestionAnswerEvent);

    T visit(OrganisationEditAddVolunteerEvent organisationEditAddVolunteerEvent);

    T visit(OrganisationEditDefaultAddressEvent organisationEditDefaultAddressEvent);

    T visit(OrganisationEditDeleteAddressEvent organisationEditDeleteAddressEvent);

    T visit(OrganisationEditDeleteAttendanceTimeEvent organisationEditDeleteAttendanceTimeEvent);

    T visit(OrganisationEditDeleteContactPersonEvent organisationEditDeleteContactPersonEvent);

    T visit(OrganisationEditDeleteGroupEvent organisationEditDeleteGroupEvent);

    T visit(OrganisationEditDeletePictureEvent organisationEditDeletePictureEvent);

    T visit(OrganisationEditDeleteQuestionAnswerEvent organisationEditDeleteQuestionAnswerEvent);

    T visit(OrganisationEditDeleteVolunteerEvent organisationEditDeleteVolunteerEvent);

    T visit(OrganisationEditDescriptionEvent organisationEditDescriptionEvent);

    T visit(OrganisationEditLogoEvent organisationEditLogoEvent);

    T visit(OrganisationEditNameEvent organisationEditNameEvent);

    T visit(OrganisationEditTeaserImageEvent organisationEditTeaserImageEvent);

    T visit(OrganisationEditUrlNameEvent organisationEditUrlNameEvent);

    T visit(OrganisationEditWebsiteEvent organisationEditWebsiteEvent);

    T visit(ProposedChangeOrganizationEvent proposedChangeOrganizationEvent);

    T visit(ConfirmedChangeOrganizationEvent confirmedChangeOrganizationEvent);
}
