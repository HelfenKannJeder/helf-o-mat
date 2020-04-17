package de.helfenkannjeder.helfomat.core.organization.event;

import de.helfenkannjeder.helfomat.core.organization.Address;
import de.helfenkannjeder.helfomat.core.organization.Organization;
import de.helfenkannjeder.helfomat.core.organization.OrganizationEventVisitor;
import de.helfenkannjeder.helfomat.core.organization.OrganizationId;

/**
 * @author Valentin Zickner
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
public class OrganizationEditAddAddressEvent extends OrganizationEditEvent {
    private int index;
    private Address address;

    protected OrganizationEditAddAddressEvent() {
    }

    public OrganizationEditAddAddressEvent(OrganizationId organizationId, int index, Address address) {
        super(organizationId);
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
    public Organization.Builder applyOnOrganizationBuilder(Organization.Builder organization) {
        return organization.addAddress(index, address);
    }

    @Override
    public <T> T visit(OrganizationEventVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
