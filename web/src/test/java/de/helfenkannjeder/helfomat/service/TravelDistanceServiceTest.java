package de.helfenkannjeder.helfomat.service;

import de.helfenkannjeder.helfomat.domain.Address;
import de.helfenkannjeder.helfomat.domain.GeoPoint;
import de.helfenkannjeder.helfomat.domain.Organisation;
import de.helfenkannjeder.helfomat.dto.TravelDistanceDto;
import de.helfenkannjeder.helfomat.dto.TravelModeDto;
import de.helfenkannjeder.helfomat.service.google.GoogleMapsDistanceMatrixService;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TravelDistanceServiceTest {

    private final GoogleMapsDistanceMatrixService distanceMatrixService = mock(GoogleMapsDistanceMatrixService.class);
    private final TravelDistanceService travelDistanceService = new TravelDistanceService(distanceMatrixService);
    private static TravelDistanceDto DUMMY_TRAVEL_DISTANCE = new TravelDistanceDto();

    static {
        DUMMY_TRAVEL_DISTANCE.setTravelMode(TravelModeDto.CYCLING);
        DUMMY_TRAVEL_DISTANCE.setDistanceInMeters(34200L);
        DUMMY_TRAVEL_DISTANCE.setTimeInSeconds(31200L);
    }

    @Test
    public void searchReturnsValuesForAllPossibleTravelOptions() throws Exception {
        when(distanceMatrixService.getTravelDistanceFor(any(), any(), any())).thenReturn(DUMMY_TRAVEL_DISTANCE);

        List<TravelDistanceDto> travelOptions = testSearch();

        assertThat(travelOptions)
            .isNotNull()
            .hasSize(TravelModeDto.values().length);
    }

    @Test
    public void onlyReturnDistancesForFoundRoutes() throws Exception {
        when(distanceMatrixService.getTravelDistanceFor(eq(TravelModeDto.TRANSIT), any(), any())).thenReturn(null);
        when(distanceMatrixService.getTravelDistanceFor(not(eq(TravelModeDto.TRANSIT)), any(), any())).thenReturn(DUMMY_TRAVEL_DISTANCE);

        List<TravelDistanceDto> travelOptions = testSearch();

        assertThat(travelOptions)
            .isNotNull()
            .hasSize(TravelModeDto.values().length - 1);
        assertThat(
            travelOptions.stream()
                .filter(travelOption -> travelOption.getTravelMode() == TravelModeDto.TRANSIT)
                .collect(Collectors.toList())
        )
            .isEmpty();
    }

    private List<TravelDistanceDto> testSearch() {
        Address address = new Address();
        address.setLocation(new GeoPoint(49.0388109, 8.3433651));
        Organisation organisation = new Organisation();
        organisation.setAddresses(Collections.singletonList(address));

        return travelDistanceService.requestTravelDistances(organisation, new GeoPoint(48.9808278, 8.4907565));
    }

}