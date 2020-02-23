package de.helfenkannjeder.helfomat.api.organisation.event;

import de.helfenkannjeder.helfomat.api.organisation.AddressDto;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;

/**
 * @author Valentin Zickner
 */
public class OrganizationEditDefaultAddressEventDto extends OrganizationEventDto {
    private AddressDto address;

    OrganizationEditDefaultAddressEventDto() {
    }

    public OrganizationEditDefaultAddressEventDto(OrganisationId organisationId, AddressDto address) {
        super(organisationId);
        this.address = address;
    }

    public AddressDto getAddress() {
        return address;
    }
}
