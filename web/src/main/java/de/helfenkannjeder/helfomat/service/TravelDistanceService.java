package de.helfenkannjeder.helfomat.service;

import de.helfenkannjeder.helfomat.domain.GeoPoint;
import de.helfenkannjeder.helfomat.domain.Organisation;
import de.helfenkannjeder.helfomat.dto.TravelDistanceDto;
import de.helfenkannjeder.helfomat.dto.TravelModeDto;
import de.helfenkannjeder.helfomat.service.google.DistanceMatrixService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class TravelDistanceService {

    private final DistanceMatrixService distanceMatrixService;

    @Autowired
    public TravelDistanceService(DistanceMatrixService distanceMatrixService) {
        this.distanceMatrixService = distanceMatrixService;
    }

    public List<TravelDistanceDto> requestTravelDistances(Organisation organisation, GeoPoint origin) {
        GeoPoint destination = determineAddress(organisation);
        return Stream.of(TravelModeDto.values())
            .map(travelMode -> distanceMatrixService.getTravelDistanceFor(travelMode, origin, destination))
            .filter(Objects::nonNull)
            .sorted(Comparator.comparingLong(TravelDistanceDto::getTimeInSeconds))
            .collect(Collectors.toList());
    }

    private GeoPoint determineAddress(Organisation organisation) {
        //TODO: default address selection
        return organisation.getAddresses().get(0).getLocation();
    }

}
