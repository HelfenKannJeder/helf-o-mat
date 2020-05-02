package de.helfenkannjeder.helfomat.api.organization

import de.helfenkannjeder.helfomat.api.geopoint.DistanceMatrixApplicationService
import de.helfenkannjeder.helfomat.api.geopoint.TravelDistanceDto
import de.helfenkannjeder.helfomat.api.geopoint.TravelModeDto
import de.helfenkannjeder.helfomat.core.geopoint.GeoPoint
import de.helfenkannjeder.helfomat.core.organization.OrganizationId
import de.helfenkannjeder.helfomat.core.organization.OrganizationRepository
import org.springframework.stereotype.Service

@Service
class TravelDistanceApplicationService(
    private val distanceMatrixApplicationService: DistanceMatrixApplicationService,
    private val organizationRepository: OrganizationRepository
) {

    fun requestTravelDistances(organizationId: OrganizationId, origin: GeoPoint): List<TravelDistanceDto> {
        val organization = organizationRepository.findOne(organizationId.value) ?: return emptyList()
        val destination = organization.defaultAddress.location
        return TravelModeDto.values()
            .map { distanceMatrixApplicationService.getTravelDistanceFor(it, origin, destination) }
            .filterNotNull()
            .sortedBy { it.timeInSeconds }
    }

}