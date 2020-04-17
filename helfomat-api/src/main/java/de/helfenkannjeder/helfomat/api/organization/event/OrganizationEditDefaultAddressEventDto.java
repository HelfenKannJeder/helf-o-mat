package de.helfenkannjeder.helfomat.api.organization.event;

import de.helfenkannjeder.helfomat.api.organization.AddressDto;
import de.helfenkannjeder.helfomat.core.organization.OrganizationId;

/**
 * @author Valentin Zickner
 */
public class OrganizationEditDefaultAddressEventDto extends OrganizationEventDto {
    private AddressDto address;

    OrganizationEditDefaultAddressEventDto() {
    }

    public OrganizationEditDefaultAddressEventDto(OrganizationId organizationId, AddressDto address) {
        super(organizationId);
        this.address = address;
    }

    public AddressDto getAddress() {
        return address;
    }

    @Override
    public <T> T visit(OrganizationEventDtoVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
