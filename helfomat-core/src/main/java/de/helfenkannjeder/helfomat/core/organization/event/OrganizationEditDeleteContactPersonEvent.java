package de.helfenkannjeder.helfomat.core.organization.event;

import de.helfenkannjeder.helfomat.core.organization.ContactPerson;
import de.helfenkannjeder.helfomat.core.organization.Organization;
import de.helfenkannjeder.helfomat.core.organization.OrganizationEventVisitor;
import de.helfenkannjeder.helfomat.core.organization.OrganizationId;

/**
 * @author Valentin Zickner
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
public class OrganizationEditDeleteContactPersonEvent extends OrganizationEditEvent {
    private ContactPerson contactPerson;

    protected OrganizationEditDeleteContactPersonEvent() {
    }

    public OrganizationEditDeleteContactPersonEvent(OrganizationId organizationId, ContactPerson contactPerson) {
        super(organizationId);
        this.contactPerson = contactPerson;
    }

    public ContactPerson getContactPerson() {
        return contactPerson;
    }

    @Override
    public Organization.Builder applyOnOrganizationBuilder(Organization.Builder organization) {
        return organization.removeContactPerson(contactPerson);
    }

    @Override
    public <T> T visit(OrganizationEventVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return "OrganizationEditDeleteContactPersonEvent{" +
            "contactPerson=" + contactPerson +
            '}';
    }
}
