package de.helfenkannjeder.helfomat.service.google;

/**
 * @author Valentin Zickner
 */
public class TravelDistanceNotRetrievedException extends RuntimeException {

    TravelDistanceNotRetrievedException(String message) {
        super(message);
    }

    TravelDistanceNotRetrievedException(Throwable cause) {
        super("Travel distance can not be retrieved.", cause);
    }

}
