package de.helfenkannjeder.helfomat.core.organisation.event;

import de.helfenkannjeder.helfomat.core.organisation.Address;
import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;

/**
 * @author Valentin Zickner
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
public class OrganisationEditDefaultAddressEvent extends OrganisationEditEvent {
    private Address defaultAddress;

    protected OrganisationEditDefaultAddressEvent() {
    }

    public OrganisationEditDefaultAddressEvent(OrganisationId organisationId, Address defaultAddress) {
        super(organisationId);
        this.defaultAddress = defaultAddress;
    }

    @Override
    public Organisation.Builder applyOnOrganisationBuilder(Organisation.Builder organisation) {
        return organisation.setDefaultAddress(defaultAddress);
    }
}
