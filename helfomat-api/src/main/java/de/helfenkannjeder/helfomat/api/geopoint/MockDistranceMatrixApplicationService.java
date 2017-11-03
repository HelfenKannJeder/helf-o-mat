package de.helfenkannjeder.helfomat.api.geopoint;

import de.helfenkannjeder.helfomat.core.ProfileRegistry;
import de.helfenkannjeder.helfomat.core.geopoint.GeoPoint;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@Profile(ProfileRegistry.MOCK_DISTANCE_CALCULATION)
@Primary
public class MockDistranceMatrixApplicationService implements DistanceMatrixApplicationService {

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
