package de.helfenkannjeder.helfomat.api.organisation.event;

import de.helfenkannjeder.helfomat.api.organisation.AddressDto;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;

/**
 * @author Valentin Zickner
 */
public class OrganizationEditDeleteAddressEventDto extends OrganizationEventDto {

    private AddressDto address;

    OrganizationEditDeleteAddressEventDto() {
    }

    public OrganizationEditDeleteAddressEventDto(OrganisationId organisationId, AddressDto address) {
        super(organisationId);
        this.address = address;
    }

    public AddressDto getAddress() {
        return address;
    }
}
