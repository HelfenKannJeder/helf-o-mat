package de.helfenkannjeder.helfomat.service.google;

import de.helfenkannjeder.helfomat.domain.GeoPoint;
import de.helfenkannjeder.helfomat.dto.TravelDistanceDto;
import de.helfenkannjeder.helfomat.dto.TravelDistanceDtoMother;
import de.helfenkannjeder.helfomat.dto.TravelModeDto;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
@Primary
public class DistranceMatrixServiceStub implements DistanceMatrixService {

    @Override
    public TravelDistanceDto getTravelDistanceFor(TravelModeDto travelMode, GeoPoint origin, GeoPoint destination) {
        return TravelDistanceDtoMother.withModeAndRandomValues(travelMode);
    }
}
