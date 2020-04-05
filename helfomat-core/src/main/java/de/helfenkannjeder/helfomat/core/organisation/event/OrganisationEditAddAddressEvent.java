package de.helfenkannjeder.helfomat.core.organisation.event;

import de.helfenkannjeder.helfomat.core.organisation.Address;
import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;
import de.helfenkannjeder.helfomat.core.organisation.OrganizationEventVisitor;

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

    public int getIndex() {
        return index;
    }

    public Address getAddress() {
        return address;
    }

    @Override
    public Organisation.Builder applyOnOrganisationBuilder(Organisation.Builder organisation) {
        return organisation.addAddress(index, address);
    }

    @Override
    public <T> T visit(OrganizationEventVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
