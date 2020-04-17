package de.helfenkannjeder.helfomat.core.organization;

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
import de.helfenkannjeder.helfomat.core.organization.event.ProposedChangeOrganizationEvent;

public interface OrganizationEventVisitor<T> {

    T visit(OrganizationCreateEvent organizationCreateEvent);

    T visit(OrganizationDeleteEvent organizationDeleteEvent);

    T visit(OrganizationEditAddAddressEvent organizationEditAddAddressEvent);

    T visit(OrganizationEditAddAttendanceTimeEvent organizationEditAddAttendanceTimeEvent);

    T visit(OrganizationEditAddContactPersonEvent organizationEditAddContactPersonEvent);

    T visit(OrganizationEditAddGroupEvent organizationEditAddGroupEvent);

    T visit(OrganizationEditAddPictureEvent organizationEditAddPictureEvent);

    T visit(OrganizationEditAddQuestionAnswerEvent organizationEditAddQuestionAnswerEvent);

    T visit(OrganizationEditAddVolunteerEvent organizationEditAddVolunteerEvent);

    T visit(OrganizationEditDefaultAddressEvent organizationEditDefaultAddressEvent);

    T visit(OrganizationEditDeleteAddressEvent organizationEditDeleteAddressEvent);

    T visit(OrganizationEditDeleteAttendanceTimeEvent organizationEditDeleteAttendanceTimeEvent);

    T visit(OrganizationEditDeleteContactPersonEvent organizationEditDeleteContactPersonEvent);

    T visit(OrganizationEditDeleteGroupEvent organizationEditDeleteGroupEvent);

    T visit(OrganizationEditDeletePictureEvent organizationEditDeletePictureEvent);

    T visit(OrganizationEditDeleteQuestionAnswerEvent organizationEditDeleteQuestionAnswerEvent);

    T visit(OrganizationEditDeleteVolunteerEvent organizationEditDeleteVolunteerEvent);

    T visit(OrganizationEditDescriptionEvent organizationEditDescriptionEvent);

    T visit(OrganizationEditLogoEvent organizationEditLogoEvent);

    T visit(OrganizationEditNameEvent organizationEditNameEvent);

    T visit(OrganizationEditTeaserImageEvent organizationEditTeaserImageEvent);

    T visit(OrganizationEditUrlNameEvent organizationEditUrlNameEvent);

    T visit(OrganizationEditWebsiteEvent organizationEditWebsiteEvent);

    T visit(ProposedChangeOrganizationEvent proposedChangeOrganizationEvent);

    T visit(ConfirmedChangeOrganizationEvent confirmedChangeOrganizationEvent);
}
