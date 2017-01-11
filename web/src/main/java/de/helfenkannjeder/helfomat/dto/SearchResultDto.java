package de.helfenkannjeder.helfomat.dto;

import java.util.List;
import java.util.Map;

/**
 * @author Valentin Zickner
 */
public class SearchResultDto {
    private final List<Map<String, Object>> organisations; // TODO: Cleanup structure with domain objects equivalent to frontend objects
    private final List<ClusteredGeoPointDto> clusteredOrganisations;

    public SearchResultDto(List<Map<String, Object>> organisations, List<ClusteredGeoPointDto> clusteredOrganisations) {
        this.organisations = organisations;
        this.clusteredOrganisations = clusteredOrganisations;
    }

    public List<Map<String, Object>> getOrganisations() {
        return organisations;
    }

    public List<ClusteredGeoPointDto> getClusteredOrganisations() {
        return clusteredOrganisations;
    }
}
