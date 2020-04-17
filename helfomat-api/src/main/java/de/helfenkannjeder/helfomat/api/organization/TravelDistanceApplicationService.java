package de.helfenkannjeder.helfomat.api.organization;

import de.helfenkannjeder.helfomat.api.geopoint.DistanceMatrixApplicationService;
import de.helfenkannjeder.helfomat.api.geopoint.TravelDistanceDto;
import de.helfenkannjeder.helfomat.api.geopoint.TravelModeDto;
import de.helfenkannjeder.helfomat.core.geopoint.GeoPoint;
import de.helfenkannjeder.helfomat.core.organization.Organization;
import de.helfenkannjeder.helfomat.core.organization.OrganizationId;
import de.helfenkannjeder.helfomat.core.organization.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TravelDistanceApplicationService {

    private final DistanceMatrixApplicationService distanceMatrixApplicationService;
    private final OrganizationRepository organizationRepository;

    @Autowired
    public TravelDistanceApplicationService(
        DistanceMatrixApplicationService distanceMatrixApplicationService,
        OrganizationRepository organizationRepository
    ) {
        this.distanceMatrixApplicationService = distanceMatrixApplicationService;
        this.organizationRepository = organizationRepository;
    }

    public List<TravelDistanceDto> requestTravelDistances(OrganizationId organizationId, GeoPoint origin) {
        Organization organization = this.organizationRepository.findOne(organizationId.getValue());
        GeoPoint destination = determineAddress(organization);
        return Stream.of(TravelModeDto.values())
            .map(travelMode -> distanceMatrixApplicationService.getTravelDistanceFor(travelMode, origin, destination))
            .filter(Objects::nonNull)
            .sorted(Comparator.comparingLong(TravelDistanceDto::getTimeInSeconds))
            .collect(Collectors.toList());
    }

    private GeoPoint determineAddress(Organization organization) {
        return organization.getDefaultAddress().getLocation();
    }

}
