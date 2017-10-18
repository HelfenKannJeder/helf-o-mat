package de.helfenkannjeder.helfomat.service.google;

import de.helfenkannjeder.helfomat.ProfileRegistry;
import de.helfenkannjeder.helfomat.domain.GeoPoint;
import de.helfenkannjeder.helfomat.dto.TravelDistanceDto;
import de.helfenkannjeder.helfomat.dto.TravelModeDto;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@Profile(ProfileRegistry.MOCK_GOOGLE_MAPS)
@Primary
public class MockDistranceMatrixService implements DistanceMatrixService {

    private static final Random RANDOM = new Random();

    @Override
    public TravelDistanceDto getTravelDistanceFor(TravelModeDto travelMode, GeoPoint origin, GeoPoint destination) {
        TravelDistanceDto travelDistanceDto = new TravelDistanceDto();
        travelDistanceDto.setTravelMode(travelMode);
        travelDistanceDto.setDistanceInMeters(Math.abs(RANDOM.nextLong() % 5000) + 1);
        travelDistanceDto.setTimeInSeconds(Math.abs(RANDOM.nextLong() % 5000) + 1);
        return travelDistanceDto;
    }

}
