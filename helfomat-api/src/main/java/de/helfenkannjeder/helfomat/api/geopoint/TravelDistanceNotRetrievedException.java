package de.helfenkannjeder.helfomat.api.geopoint;

/**
 * @author Valentin Zickner
 */
public class TravelDistanceNotRetrievedException extends RuntimeException {

    public TravelDistanceNotRetrievedException(String message) {
        super(message);
    }

    public TravelDistanceNotRetrievedException(Throwable cause) {
        super("Travel distance can not be retrieved.", cause);
    }

}
