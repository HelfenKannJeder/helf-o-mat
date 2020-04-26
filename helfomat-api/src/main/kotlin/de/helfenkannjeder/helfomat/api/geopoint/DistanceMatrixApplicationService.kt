package de.helfenkannjeder.helfomat.api.geopoint

import de.helfenkannjeder.helfomat.core.geopoint.GeoPoint

interface DistanceMatrixApplicationService {
    fun getTravelDistanceFor(travelMode: TravelModeDto, origin: GeoPoint, destination: GeoPoint): TravelDistanceDto?
}