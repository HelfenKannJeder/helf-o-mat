package de.helfenkannjeder.helfomat.core.organisation.event;

import de.helfenkannjeder.helfomat.core.organisation.Address;
import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;

/**
 * @author Valentin Zickner
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
public class OrganisationEditAddAddressEvent extends OrganisationEditEvent {
    private int index;
    private Address address;

    protected OrganisationEditAddAddressEvent() {
    }

    public OrganisationEditAddAddressEvent(OrganisationId organisationId, int index, Address address) {
        super(organisationId);
        this.index = index;
        this.address = address;
    }

    @Override
    public Organisation.Builder applyOnOrganisationBuilder(Organisation.Builder organisation) {
        return organisation.addAddress(index, address);
    }
}
