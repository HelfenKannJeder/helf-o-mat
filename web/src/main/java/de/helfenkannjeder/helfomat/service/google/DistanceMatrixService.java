package de.helfenkannjeder.helfomat.service.google;

import de.helfenkannjeder.helfomat.domain.GeoPoint;
import de.helfenkannjeder.helfomat.dto.TravelDistanceDto;
import de.helfenkannjeder.helfomat.dto.TravelModeDto;
import org.springframework.stereotype.Component;

@Component
public interface DistanceMatrixService {
    TravelDistanceDto getTravelDistanceFor(TravelModeDto travelMode, GeoPoint origin, GeoPoint destination);
}
