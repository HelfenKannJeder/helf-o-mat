package de.helfenkannjeder.helfomat.service.google;

import com.google.maps.GeoApiContext;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.DistanceMatrixElementStatus;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;
import com.google.maps.model.Unit;
import de.helfenkannjeder.helfomat.domain.GeoPoint;
import de.helfenkannjeder.helfomat.dto.TravelDistanceDto;
import de.helfenkannjeder.helfomat.dto.TravelModeDto;
import de.helfenkannjeder.helfomat.service.TravelDistanceService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DistanceMatrixServiceImpl implements DistanceMatrixService {

    private static final Logger LOGGER = Logger.getLogger(TravelDistanceService.class);

    private final GeoApiContext geoApiContext;

    public DistanceMatrixServiceImpl(@Value("${googlemaps.apiKey}")String mapsApiKey) {
        this.geoApiContext = new GeoApiContext.Builder()
            .apiKey(mapsApiKey)
            .build();
    }

    @Override
    public TravelDistanceDto getTravelDistanceFor(TravelModeDto travelMode, GeoPoint origin, GeoPoint destination) {
        try {
            DistanceMatrix apiResult = com.google.maps.DistanceMatrixApi.newRequest(geoApiContext)
                .destinations(geoPointToLatLng(destination))
                .origins(geoPointToLatLng(origin))
                .mode(mapToGoogleTravelMode(travelMode))
                .units(Unit.METRIC)
                .await();

            return mapApiResult(apiResult, travelMode);
        } catch (Exception e) {
            LOGGER.error("Travel distance can not be retrieved.", e);
            throw new RuntimeException(e);
        }
    }

    private TravelDistanceDto mapApiResult(DistanceMatrix apiResult, TravelModeDto travelMode) {
        if (apiResult.rows.length != 1 || apiResult.rows[0].elements.length != 1) {
            throw new RuntimeException("Invalid distance matrix result");
        }
        DistanceMatrixElement resultElement = apiResult.rows[0].elements[0];

        //TODO: map location not found to bad request?

        if (resultElement.status.equals(DistanceMatrixElementStatus.OK)) {
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

        throw new RuntimeException("Invalid travel mode");
    }

    private LatLng geoPointToLatLng(GeoPoint geoPoint) {
        return new LatLng(geoPoint.getLat(), geoPoint.getLon());
    }
}
