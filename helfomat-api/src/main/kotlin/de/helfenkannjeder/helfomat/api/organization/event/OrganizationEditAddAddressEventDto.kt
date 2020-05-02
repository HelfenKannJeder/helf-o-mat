package de.helfenkannjeder.helfomat.api.organization.event;

import de.helfenkannjeder.helfomat.api.organization.AddressDto;
import de.helfenkannjeder.helfomat.core.organization.OrganizationId;

/**
 * @author Valentin Zickner
 */
public class OrganizationEditAddAddressEventDto extends OrganizationEventDto {

    private int index;
    private AddressDto address;

    OrganizationEditAddAddressEventDto() {
    }

    public OrganizationEditAddAddressEventDto(OrganizationId organizationId, int index, AddressDto address) {
        super(organizationId);
        this.index = index;
        this.address = address;
    }

    public int getIndex() {
        return index;
    }

    public AddressDto getAddress() {
        return address;
    }

    @Override
    public <T> T visit(OrganizationEventDtoVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
