package de.helfenkannjeder.helfomat.api.geopoint;

import de.helfenkannjeder.helfomat.core.geopoint.GeoPoint;

public interface DistanceMatrixApplicationService {
    TravelDistanceDto getTravelDistanceFor(TravelModeDto travelMode, GeoPoint origin, GeoPoint destination) throws InvalidTravelModeException;
}
