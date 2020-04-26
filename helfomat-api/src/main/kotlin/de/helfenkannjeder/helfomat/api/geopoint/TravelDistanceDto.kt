package de.helfenkannjeder.helfomat.api.geopoint

data class TravelDistanceDto (
    val travelMode: TravelModeDto,
    val timeInSeconds: Long,
    val distanceInMeters: Long
)