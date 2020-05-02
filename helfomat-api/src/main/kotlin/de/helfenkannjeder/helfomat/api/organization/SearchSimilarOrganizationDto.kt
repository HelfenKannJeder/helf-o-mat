package de.helfenkannjeder.helfomat.api.organization;

import de.helfenkannjeder.helfomat.core.organization.OrganizationType;

/**
 * @author Valentin Zickner
 */
public class SearchSimilarOrganizationDto {

    private AddressDto address;
    private OrganizationType organizationType;
    private Long distanceInMeters;

    private SearchSimilarOrganizationDto() {
    }

    public SearchSimilarOrganizationDto(AddressDto address, OrganizationType organizationType, Long distanceInMeters) {
        this.address = address;
        this.organizationType = organizationType;
        this.distanceInMeters = distanceInMeters;
    }

    public AddressDto getAddress() {
        return address;
    }

    public OrganizationType getOrganizationType() {
        return organizationType;
    }

    public Long getDistanceInMeters() {
        return distanceInMeters;
    }
}
