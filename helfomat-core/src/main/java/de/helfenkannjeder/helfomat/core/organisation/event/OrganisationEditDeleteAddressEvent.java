package de.helfenkannjeder.helfomat.core.organisation.event;

import de.helfenkannjeder.helfomat.core.organisation.Address;
import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;
import de.helfenkannjeder.helfomat.core.organisation.OrganizationEventVisitor;

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

    @Override
    public <T> T visit(OrganizationEventVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
