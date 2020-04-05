package de.helfenkannjeder.helfomat.core.organisation.event;

import de.helfenkannjeder.helfomat.core.organisation.Address;
import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;

/**
 * @author Valentin Zickner
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
public class OrganisationEditDeleteAddressEvent extends OrganisationEditEvent {
    private Address address;

    protected OrganisationEditDeleteAddressEvent() {
    }

    public OrganisationEditDeleteAddressEvent(OrganisationId organisationId, Address address) {
        super(organisationId);
        this.address = address;
    }

    public Address getAddress() {
        return address;
    }

    @Override
    public Organisation.Builder applyOnOrganisationBuilder(Organisation.Builder organisation) {
        return organisation.removeAddress(address);
    }
}