package de.helfenkannjeder.helfomat.core.organization.event;

import de.helfenkannjeder.helfomat.core.organization.Organization;
import de.helfenkannjeder.helfomat.core.organization.OrganizationEventVisitor;
import de.helfenkannjeder.helfomat.core.organization.OrganizationId;
import de.helfenkannjeder.helfomat.core.organization.Volunteer;

/**
 * @author Valentin Zickner
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
public class OrganizationEditDeleteVolunteerEvent extends OrganizationEditEvent {
    private Volunteer volunteer;

    protected OrganizationEditDeleteVolunteerEvent() {
    }

    public OrganizationEditDeleteVolunteerEvent(OrganizationId organizationId, Volunteer volunteer) {
        super(organizationId);
        this.volunteer = volunteer;
    }

    public Volunteer getVolunteer() {
        return volunteer;
    }

    @Override
    public Organization.Builder applyOnOrganizationBuilder(Organization.Builder organization) {
        return organization.removeVolunteer(volunteer);
    }

    @Override
    public <T> T visit(OrganizationEventVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return "OrganizationEditDeleteVolunteerEvent{" +
            "volunteer=" + volunteer +
            '}';
    }
}
