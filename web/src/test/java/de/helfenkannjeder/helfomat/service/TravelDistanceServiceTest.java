package de.helfenkannjeder.helfomat.service;

import de.helfenkannjeder.helfomat.domain.Address;
import de.helfenkannjeder.helfomat.domain.GeoPoint;
import de.helfenkannjeder.helfomat.domain.Organisation;
import de.helfenkannjeder.helfomat.dto.TravelDistanceDto;
import de.helfenkannjeder.helfomat.dto.TravelDistanceDtoMother;
import de.helfenkannjeder.helfomat.dto.TravelModeDto;
import de.helfenkannjeder.helfomat.service.google.DistanceMatrixServiceImpl;
import org.junit.Test;
import org.mockito.stubbing.Answer;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TravelDistanceServiceTest {

    private final DistanceMatrixServiceImpl distanceMatrixService = mock(DistanceMatrixServiceImpl.class);
    private final TravelDistanceService travelDistanceService = new TravelDistanceService(distanceMatrixService);

    @Test
    public void searchReturnsValuesForAllPossibleTravelOptions() throws Exception {
        when(distanceMatrixService.getTravelDistanceFor(any(), any(), any())).thenAnswer((Answer<TravelDistanceDto>) invocationOnMock -> {
            TravelModeDto requestedTravelMode = invocationOnMock.getArgumentAt(0, TravelModeDto.class);
            return TravelDistanceDtoMother.withModeAndRandomValues(requestedTravelMode);
        });

        List<TravelDistanceDto> travelOptions = testSearch();

        assertThat(travelOptions.size(), is(TravelModeDto.values().length));
    }

    @Test
    public void onlyReturnDistancesForFoundRoutes() throws Exception {
        when(distanceMatrixService.getTravelDistanceFor(any(), any(), any())).thenAnswer((Answer<TravelDistanceDto>) invocationOnMock -> {
            TravelModeDto requestedTravelMode = invocationOnMock.getArgumentAt(0, TravelModeDto.class);

            // Distance matrix api is not able to find a route for travel mode transit
            if(requestedTravelMode.equals(TravelModeDto.TRANSIT)) {
                return null;
            }

            return TravelDistanceDtoMother.withModeAndRandomValues(requestedTravelMode);
        });

        List<TravelDistanceDto> travelOptions = testSearch();

        assertThat(travelOptions.size(), is(TravelModeDto.values().length-1));
        assertThat(travelOptions.stream().filter(travelOption -> travelOption.getTravelMode() == TravelModeDto.TRANSIT).collect(Collectors.toList()).size(), is(0));
    }

    private List<TravelDistanceDto> testSearch() {
        Address address = new Address();
        address.setLocation(new GeoPoint(49.0388109, 8.3433651));
        Organisation organisation = new Organisation();
        organisation.setAddresses(Collections.singletonList(address));

        return travelDistanceService.requestTravelDistances(organisation, new GeoPoint(48.9808278, 8.4907565));
    }
}