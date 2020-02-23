package de.helfenkannjeder.helfomat.core.organisation.event;

import de.helfenkannjeder.helfomat.core.organisation.Address;
import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;
import de.helfenkannjeder.helfomat.core.organisation.OrganizationEventVisitor;

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

    public Address getDefaultAddress() {
        return defaultAddress;
    }

    @Override
    public Organisation.Builder applyOnOrganisationBuilder(Organisation.Builder organisation) {
        return organisation.setDefaultAddress(defaultAddress);
    }

    @Override
    public <T> T visit(OrganizationEventVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
