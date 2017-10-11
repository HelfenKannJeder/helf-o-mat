package de.helfenkannjeder.helfomat.service;

import de.helfenkannjeder.helfomat.domain.GeoPoint;
import de.helfenkannjeder.helfomat.domain.Organisation;
import de.helfenkannjeder.helfomat.dto.TravelDistanceDto;
import de.helfenkannjeder.helfomat.dto.TravelModeDto;
import de.helfenkannjeder.helfomat.service.google.DistanceMatrixService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TravelDistanceService {

    private final DistanceMatrixService distanceMatrixService;

    @Autowired
    public TravelDistanceService(DistanceMatrixService distanceMatrixService) {
        this.distanceMatrixService = distanceMatrixService;
    }

    public List<TravelDistanceDto> requestTravelDistances(Organisation organisation, GeoPoint origin) {
        //TODO: default address selection
        GeoPoint destination = organisation.getAddresses().get(0).getLocation();
        List<TravelDistanceDto> travelOptions = new ArrayList<>();

        for (TravelModeDto travelMode : TravelModeDto.values()) {
            TravelDistanceDto maybeTravelOption = distanceMatrixService.getTravelDistanceFor(travelMode, origin, destination);
            if (maybeTravelOption != null) {
                travelOptions.add(maybeTravelOption);
            }
        }

        return travelOptions.stream()
            .sorted(Comparator.comparingLong(TravelDistanceDto::getTimeInSeconds))
            .collect(Collectors.toList());
    }
}
