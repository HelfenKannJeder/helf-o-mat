package de.helfenkannjeder.helfomat.api.organization;

import de.helfenkannjeder.helfomat.api.geopoint.DistanceMatrixApplicationService;
import de.helfenkannjeder.helfomat.api.geopoint.TravelDistanceDto;
import de.helfenkannjeder.helfomat.api.geopoint.TravelModeDto;
import de.helfenkannjeder.helfomat.core.geopoint.GeoPoint;
import de.helfenkannjeder.helfomat.core.organization.Address;
import de.helfenkannjeder.helfomat.core.organization.Organization;
import de.helfenkannjeder.helfomat.core.organization.OrganizationId;
import de.helfenkannjeder.helfomat.core.organization.OrganizationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TravelDistanceApplicationServiceTest {

    @Mock
    private DistanceMatrixApplicationService distanceMatrixService;

    @Mock
    private OrganizationRepository organizationRepository;

    private TravelDistanceApplicationService travelDistanceApplicationService;

    private static final TravelDistanceDto DUMMY_TRAVEL_DISTANCE = new TravelDistanceDto();

    @BeforeEach
    void setUp() {
        travelDistanceApplicationService = new TravelDistanceApplicationService(distanceMatrixService, organizationRepository);
    }

    static {
        DUMMY_TRAVEL_DISTANCE.setTravelMode(TravelModeDto.CYCLING);
        DUMMY_TRAVEL_DISTANCE.setDistanceInMeters(34200L);
        DUMMY_TRAVEL_DISTANCE.setTimeInSeconds(31200L);
    }

    @Test
    void searchReturnsValuesForAllPossibleTravelOptions() {
        when(distanceMatrixService.getTravelDistanceFor(any(), any(), any())).thenReturn(DUMMY_TRAVEL_DISTANCE);

        List<TravelDistanceDto> travelOptions = testSearch();

        assertThat(travelOptions)
            .isNotNull()
            .hasSize(TravelModeDto.values().length);
    }

    @Test
    void onlyReturnDistancesForFoundRoutes() {
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
        Address address = new Address.Builder()
            .setLocation(new GeoPoint(49.0388109, 8.3433651))
            .build();
        OrganizationId organizationId = new OrganizationId();
        when(organizationRepository.findOne(organizationId.getValue())).thenReturn(new Organization.Builder()
            .setDefaultAddress(address)
            .build());

        return travelDistanceApplicationService.requestTravelDistances(organizationId, new GeoPoint(48.9808278, 8.4907565));
    }

}