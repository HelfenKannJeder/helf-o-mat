package de.helfenkannjeder.helfomat.infrastructure.google.maps

import com.google.maps.DistanceMatrixApi
import com.google.maps.GeoApiContext
import com.google.maps.errors.ApiException
import com.google.maps.model.DistanceMatrix
import com.google.maps.model.DistanceMatrixElementStatus
import com.google.maps.model.LatLng
import com.google.maps.model.TravelMode
import com.google.maps.model.Unit
import de.helfenkannjeder.helfomat.api.geopoint.DistanceMatrixApplicationService
import de.helfenkannjeder.helfomat.api.geopoint.TravelDistanceDto
import de.helfenkannjeder.helfomat.api.geopoint.TravelDistanceNotRetrievedException
import de.helfenkannjeder.helfomat.api.geopoint.TravelModeDto
import de.helfenkannjeder.helfomat.core.geopoint.GeoPoint
import org.springframework.stereotype.Service
import java.io.IOException

@Service
class GoogleMapsDistanceMatrixApplicationService(googleMapsConfiguration: GoogleMapsConfiguration) : DistanceMatrixApplicationService {

    private val geoApiContext: GeoApiContext = GeoApiContext.Builder()
        .apiKey(googleMapsConfiguration.apiKey)
        .build()

    override fun getTravelDistanceFor(travelMode: TravelModeDto, origin: GeoPoint, destination: GeoPoint): TravelDistanceDto? {
        return try {
            val apiResult = DistanceMatrixApi.newRequest(geoApiContext)
                .destinations(geoPointToLatLng(destination))
                .origins(geoPointToLatLng(origin))
                .mode(mapToGoogleTravelMode(travelMode))
                .units(Unit.METRIC)
                .await()
            mapApiResult(apiResult, travelMode)
        } catch (e: ApiException) {
            throw TravelDistanceNotRetrievedException(e)
        } catch (e: InterruptedException) {
            throw TravelDistanceNotRetrievedException(e)
        } catch (e: IOException) {
            throw TravelDistanceNotRetrievedException(e)
        }
    }

    private fun mapApiResult(apiResult: DistanceMatrix, travelMode: TravelModeDto): TravelDistanceDto? {
        if (apiResult.rows.size != 1 || apiResult.rows[0].elements.size != 1) {
            throw TravelDistanceNotRetrievedException("Invalid distance matrix result")
        }
        val resultElement = apiResult.rows[0].elements[0]

        if (resultElement.status == DistanceMatrixElementStatus.OK) {
            return TravelDistanceDto(
                travelMode,
                timeInSeconds = resultElement.duration.inSeconds,
                distanceInMeters = resultElement.distance.inMeters
            )
        }

        // no route for travel mode found
        return null
    }

    private fun mapToGoogleTravelMode(internalTravelMode: TravelModeDto): TravelMode {
        return when (internalTravelMode) {
            TravelModeDto.WALKING -> TravelMode.WALKING
            TravelModeDto.CYCLING -> TravelMode.BICYCLING
            TravelModeDto.DRIVING -> TravelMode.DRIVING
            TravelModeDto.TRANSIT -> TravelMode.TRANSIT
        }
    }

    private fun geoPointToLatLng(geoPoint: GeoPoint): LatLng {
        return LatLng(geoPoint.lat, geoPoint.lon)
    }

}