package de.helfenkannjeder.helfomat.core.organisation.event;

import de.helfenkannjeder.helfomat.core.organisation.ContactPerson;
import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;

/**
 * @author Valentin Zickner
 */
public class OrganisationEditDeleteContactPersonEvent extends OrganisationEditEvent {
    private ContactPerson contactPerson;

    public OrganisationEditDeleteContactPersonEvent(OrganisationId organisationId, ContactPerson contactPerson) {
        super(organisationId);
        this.contactPerson = contactPerson;
    }

    @Override
    public Organisation.Builder applyOnOrganisationBuilder(Organisation.Builder organisation) {
        return organisation.removeContactPerson(contactPerson);
    }
}
