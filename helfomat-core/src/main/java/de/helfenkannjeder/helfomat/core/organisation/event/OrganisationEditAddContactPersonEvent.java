package de.helfenkannjeder.helfomat.core.organisation.event;

import de.helfenkannjeder.helfomat.core.organisation.ContactPerson;
import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;

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

}
