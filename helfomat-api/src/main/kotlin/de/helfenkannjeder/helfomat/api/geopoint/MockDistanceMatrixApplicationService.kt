package de.helfenkannjeder.helfomat.api.geopoint

import de.helfenkannjeder.helfomat.core.ProfileRegistry
import de.helfenkannjeder.helfomat.core.geopoint.GeoPoint
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import java.util.*

@Service
@Profile(ProfileRegistry.MOCK_DISTANCE_CALCULATION)
@Primary
class MockDistanceMatrixApplicationService : DistanceMatrixApplicationService {

    override fun getTravelDistanceFor(travelMode: TravelModeDto, origin: GeoPoint, destination: GeoPoint): TravelDistanceDto? {
        return TravelDistanceDto(
            travelMode,
            distanceInMeters = Math.abs(RANDOM.nextLong() % 5000) + 1,
            timeInSeconds = Math.abs(RANDOM.nextLong() % 5000) + 1
        )
    }

    companion object {
        private val RANDOM = Random()
    }
}