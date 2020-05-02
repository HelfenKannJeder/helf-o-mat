package de.helfenkannjeder.helfomat.api.organization

import de.helfenkannjeder.helfomat.api.geopoint.DistanceMatrixApplicationService
import de.helfenkannjeder.helfomat.api.geopoint.TravelDistanceDto
import de.helfenkannjeder.helfomat.api.geopoint.TravelModeDto
import de.helfenkannjeder.helfomat.core.geopoint.GeoPoint
import de.helfenkannjeder.helfomat.core.organization.Address
import de.helfenkannjeder.helfomat.core.organization.Organization
import de.helfenkannjeder.helfomat.core.organization.OrganizationId
import de.helfenkannjeder.helfomat.core.organization.OrganizationRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.AdditionalMatchers.not
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class TravelDistanceApplicationServiceTest {
    @Mock
    private lateinit var distanceMatrixService: DistanceMatrixApplicationService

    @Mock
    private lateinit var organizationRepository: OrganizationRepository
    private lateinit var travelDistanceApplicationService: TravelDistanceApplicationService

    @BeforeEach
    fun setUp() {
        travelDistanceApplicationService = TravelDistanceApplicationService(distanceMatrixService, organizationRepository)
    }

    @Test
    fun searchReturnsValuesForAllPossibleTravelOptions() {
        `when`(distanceMatrixService.getTravelDistanceFor(safeAny(TravelModeDto::class.java), safeAny(GeoPoint::class.java), safeAny(GeoPoint::class.java))).thenReturn(DUMMY_TRAVEL_DISTANCE)
        val travelOptions = testSearch()
        Assertions.assertThat(travelOptions)
            .isNotNull
            .hasSize(TravelModeDto.values().size)
    }

    @Test
    fun onlyReturnDistancesForFoundRoutes() {
        `when`(distanceMatrixService.getTravelDistanceFor(safeEq(TravelModeDto.TRANSIT), safeAny(GeoPoint::class.java), safeAny(GeoPoint::class.java))).thenReturn(null)
        `when`(distanceMatrixService.getTravelDistanceFor(safeNot(safeEq(TravelModeDto.TRANSIT)), safeAny(GeoPoint::class.java), safeAny(GeoPoint::class.java))).thenReturn(DUMMY_TRAVEL_DISTANCE)
        val travelOptions = testSearch()
        Assertions.assertThat(travelOptions)
            .isNotNull
            .hasSize(TravelModeDto.values().size - 1)
        Assertions.assertThat(
            travelOptions.filter { (travelMode) -> travelMode === TravelModeDto.TRANSIT }
        )
            .isEmpty()
    }

    private fun testSearch(): List<TravelDistanceDto> {
        val address = Address.Builder()
            .setLocation(GeoPoint(49.0388109, 8.3433651))
            .build()
        val organizationId = OrganizationId()
        `when`(organizationRepository.findOne(organizationId.value)).thenReturn(Organization.Builder()
            .setDefaultAddress(address)
            .build())
        return travelDistanceApplicationService.requestTravelDistances(organizationId, GeoPoint(48.9808278, 8.4907565))
    }

    companion object {
        private val DUMMY_TRAVEL_DISTANCE = TravelDistanceDto(TravelModeDto.CYCLING, 31200L, 34200L)
    }
}

fun <T : Any> safeEq(value: T): T = eq(value) ?: value
fun <T : Any> safeNot(value: T): T = not(value) ?: value
fun safeAny(value: Class<GeoPoint>): GeoPoint = any(value) ?: GeoPoint(49.0, 8.0)
fun safeAny(value: Class<TravelModeDto>): TravelModeDto = any(value) ?: TravelModeDto.CYCLING