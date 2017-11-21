package de.helfenkannjeder.helfomat.api.organisation;

import de.helfenkannjeder.helfomat.api.geopoint.DistanceMatrixApplicationService;
import de.helfenkannjeder.helfomat.api.geopoint.TravelDistanceDto;
import de.helfenkannjeder.helfomat.api.geopoint.TravelModeDto;
import de.helfenkannjeder.helfomat.core.geopoint.GeoPoint;
import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationRepository;
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
    private final OrganisationRepository organisationRepository;

    @Autowired
    public TravelDistanceApplicationService(
        DistanceMatrixApplicationService distanceMatrixApplicationService,
        OrganisationRepository organisationRepository
    ) {
        this.distanceMatrixApplicationService = distanceMatrixApplicationService;
        this.organisationRepository = organisationRepository;
    }

    public List<TravelDistanceDto> requestTravelDistances(OrganisationId organisationId, GeoPoint origin) {
        Organisation organisation = this.organisationRepository.findOne(organisationId.getValue());
        GeoPoint destination = determineAddress(organisation);
        return Stream.of(TravelModeDto.values())
            .map(travelMode -> distanceMatrixApplicationService.getTravelDistanceFor(travelMode, origin, destination))
            .filter(Objects::nonNull)
            .sorted(Comparator.comparingLong(TravelDistanceDto::getTimeInSeconds))
            .collect(Collectors.toList());
    }

    private GeoPoint determineAddress(Organisation organisation) {
        return organisation.getDefaultAddress().getLocation();
    }

}
