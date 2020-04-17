package de.helfenkannjeder.helfomat.core.organization.event;

import de.helfenkannjeder.helfomat.core.organization.ContactPerson;
import de.helfenkannjeder.helfomat.core.organization.Organization;
import de.helfenkannjeder.helfomat.core.organization.OrganizationEventVisitor;
import de.helfenkannjeder.helfomat.core.organization.OrganizationId;

/**
 * @author Valentin Zickner
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
public class OrganizationEditAddContactPersonEvent extends OrganizationEditEvent {
    private int index;
    private ContactPerson contactPerson;

    protected OrganizationEditAddContactPersonEvent() {
    }

    public OrganizationEditAddContactPersonEvent(OrganizationId organizationId, int index, ContactPerson contactPerson) {
        super(organizationId);
        this.index = index;
        this.contactPerson = contactPerson;
    }

    public int getIndex() {
        return index;
    }

    public ContactPerson getContactPerson() {
        return contactPerson;
    }

    @Override
    public Organization.Builder applyOnOrganizationBuilder(Organization.Builder organization) {
        return organization.addContactPerson(index, contactPerson);
    }

    @Override
    public <T> T visit(OrganizationEventVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
