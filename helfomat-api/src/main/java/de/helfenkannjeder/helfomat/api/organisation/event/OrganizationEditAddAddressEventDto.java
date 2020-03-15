package de.helfenkannjeder.helfomat.api.organisation.event;

import de.helfenkannjeder.helfomat.api.organisation.AddressDto;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;

/**
 * @author Valentin Zickner
 */
public class OrganizationEditAddAddressEventDto extends OrganizationEventDto {

    private int index;
    private AddressDto address;

    OrganizationEditAddAddressEventDto() {
    }

    public OrganizationEditAddAddressEventDto(OrganisationId organisationId, int index, AddressDto address) {
        super(organisationId);
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
