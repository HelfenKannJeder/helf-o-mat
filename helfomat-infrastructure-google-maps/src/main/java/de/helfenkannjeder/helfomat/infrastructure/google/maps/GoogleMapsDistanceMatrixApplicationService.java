package de.helfenkannjeder.helfomat.infrastructure.google.maps;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.DistanceMatrixElementStatus;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;
import com.google.maps.model.Unit;
import de.helfenkannjeder.helfomat.api.geopoint.DistanceMatrixApplicationService;
import de.helfenkannjeder.helfomat.api.geopoint.InvalidTravelModeException;
import de.helfenkannjeder.helfomat.api.geopoint.TravelDistanceDto;
import de.helfenkannjeder.helfomat.api.geopoint.TravelDistanceNotRetrievedException;
import de.helfenkannjeder.helfomat.api.geopoint.TravelModeDto;
import de.helfenkannjeder.helfomat.core.geopoint.GeoPoint;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;

@Service
public class GoogleMapsDistanceMatrixApplicationService implements DistanceMatrixApplicationService {

    private final GeoApiContext geoApiContext;

    public GoogleMapsDistanceMatrixApplicationService(GoogleMapsConfiguration googleMapsConfiguration) {
        this.geoApiContext = new GeoApiContext.Builder()
            .apiKey(googleMapsConfiguration.getApiKey())
            .build();
    }

    @Override
    public TravelDistanceDto getTravelDistanceFor(TravelModeDto travelMode, GeoPoint origin, GeoPoint destination) {
        try {
            DistanceMatrix apiResult = DistanceMatrixApi.newRequest(geoApiContext)
                .destinations(geoPointToLatLng(destination))
                .origins(geoPointToLatLng(origin))
                .mode(mapToGoogleTravelMode(travelMode))
                .units(Unit.METRIC)
                .await();

            return mapApiResult(apiResult, travelMode);
        } catch (ApiException | InterruptedException | IOException e) {
            throw new TravelDistanceNotRetrievedException(e);
        }
    }

    private TravelDistanceDto mapApiResult(DistanceMatrix apiResult, TravelModeDto travelMode) {
        if (apiResult.rows.length != 1 || apiResult.rows[0].elements.length != 1) {
            throw new TravelDistanceNotRetrievedException("Invalid distance matrix result");
        }
        DistanceMatrixElement resultElement = apiResult.rows[0].elements[0];

        //TODO: map location not found to bad request?

        if (Objects.equals(resultElement.status, DistanceMatrixElementStatus.OK)) {
            TravelDistanceDto travelDistance = new TravelDistanceDto();
            travelDistance.setTravelMode(travelMode);
            travelDistance.setDistanceInMeters(resultElement.distance.inMeters);
            travelDistance.setTimeInSeconds(resultElement.duration.inSeconds);
            return travelDistance;
        }

        // no route for travel mode found
        return null;
    }


    private TravelMode mapToGoogleTravelMode(TravelModeDto internalTravelMode) {
        switch (internalTravelMode) {
            case WALKING:
                return TravelMode.WALKING;
            case CYCLING:
                return TravelMode.BICYCLING;
            case DRIVING:
                return TravelMode.DRIVING;
            case TRANSIT:
                return TravelMode.TRANSIT;
        }

        throw new InvalidTravelModeException(internalTravelMode.name());
    }

    private LatLng geoPointToLatLng(GeoPoint geoPoint) {
        return new LatLng(geoPoint.getLat(), geoPoint.getLon());
    }
}
