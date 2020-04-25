package de.helfenkannjeder.helfomat.core.organization.event;

import de.helfenkannjeder.helfomat.core.organization.Address;
import de.helfenkannjeder.helfomat.core.organization.Organization;
import de.helfenkannjeder.helfomat.core.organization.OrganizationEventVisitor;
import de.helfenkannjeder.helfomat.core.organization.OrganizationId;

/**
 * @author Valentin Zickner
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
public class OrganizationEditDefaultAddressEvent extends OrganizationEditEvent {
    private Address defaultAddress;

    protected OrganizationEditDefaultAddressEvent() {
    }

    public OrganizationEditDefaultAddressEvent(OrganizationId organizationId, Address defaultAddress) {
        super(organizationId);
        this.defaultAddress = defaultAddress;
    }

    public Address getDefaultAddress() {
        return defaultAddress;
    }

    @Override
    public Organization.Builder applyOnOrganizationBuilder(Organization.Builder organization) {
        return organization.setDefaultAddress(defaultAddress);
    }

    @Override
    public <T> T visit(OrganizationEventVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return "OrganizationEditDefaultAddressEvent{" +
            "defaultAddress=" + defaultAddress +
            '}';
    }
}
