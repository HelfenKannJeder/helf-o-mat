package de.helfenkannjeder.helfomat.core.organisation.event;

import de.helfenkannjeder.helfomat.core.organisation.ContactPerson;
import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;
import de.helfenkannjeder.helfomat.core.organisation.OrganizationEventVisitor;

/**
 * @author Valentin Zickner
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
public class OrganisationEditAddContactPersonEvent extends OrganisationEditEvent {
    private int index;
    private ContactPerson contactPerson;

    protected OrganisationEditAddContactPersonEvent() {
    }

    public OrganisationEditAddContactPersonEvent(OrganisationId organisationId, int index, ContactPerson contactPerson) {
        super(organisationId);
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
    public Organisation.Builder applyOnOrganisationBuilder(Organisation.Builder organisation) {
        return organisation.addContactPerson(index, contactPerson);
    }

    @Override
    public <T> T visit(OrganizationEventVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
