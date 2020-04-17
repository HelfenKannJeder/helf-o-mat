package de.helfenkannjeder.helfomat.core.organization.event;

import de.helfenkannjeder.helfomat.core.organization.Organization;
import de.helfenkannjeder.helfomat.core.organization.OrganizationEventVisitor;
import de.helfenkannjeder.helfomat.core.organization.OrganizationId;
import de.helfenkannjeder.helfomat.core.organization.Volunteer;

/**
 * @author Valentin Zickner
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
public class OrganizationEditAddVolunteerEvent extends OrganizationEditEvent {
    private int index;
    private Volunteer volunteer;

    protected OrganizationEditAddVolunteerEvent() {
    }

    public OrganizationEditAddVolunteerEvent(OrganizationId organizationId, int index, Volunteer volunteer) {
        super(organizationId);
        this.index = index;
        this.volunteer = volunteer;
    }

    public int getIndex() {
        return index;
    }

    public Volunteer getVolunteer() {
        return volunteer;
    }

    @Override
    public Organization.Builder applyOnOrganizationBuilder(Organization.Builder organization) {
        return organization.addVolunteer(index, volunteer);
    }

    @Override
    public <T> T visit(OrganizationEventVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
