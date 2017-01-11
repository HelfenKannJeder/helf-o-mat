package de.helfenkannjeder.helfomat.dto;

import java.util.List;

/**
 * @author Valentin Zickner
 */
public class SearchResultDto {
    private final List<OrganisationDto> organisations;
    private final List<ClusteredGeoPointDto> clusteredOrganisations;

    public SearchResultDto(List<OrganisationDto> organisations, List<ClusteredGeoPointDto> clusteredOrganisations) {
        this.organisations = organisations;
        this.clusteredOrganisations = clusteredOrganisations;
    }

    public List<OrganisationDto> getOrganisations() {
        return organisations;
    }

    public List<ClusteredGeoPointDto> getClusteredOrganisations() {
        return clusteredOrganisations;
    }
}
