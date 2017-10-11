package de.helfenkannjeder.helfomat.dto;

import java.util.Random;

public class TravelDistanceDtoMother {
    public static TravelDistanceDto withModeAndRandomValues(TravelModeDto travelMode) {
        TravelDistanceDto travelDistanceDto = new TravelDistanceDto();
        travelDistanceDto.setTravelMode(travelMode);
        Random random = new Random();
        travelDistanceDto.setDistanceInMeters(Math.abs(random.nextLong() % 50000) + 300);
        travelDistanceDto.setTimeInSeconds(Math.abs(random.nextLong() % 50000) + 300);
        return travelDistanceDto;
    }
}
