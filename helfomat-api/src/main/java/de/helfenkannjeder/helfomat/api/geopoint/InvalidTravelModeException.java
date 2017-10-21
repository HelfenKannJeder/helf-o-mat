package de.helfenkannjeder.helfomat.api.geopoint;

/**
 * @author Valentin Zickner
 */
public class InvalidTravelModeException extends RuntimeException {
    public InvalidTravelModeException(String mode) {
        super("Invalid travel mode '" + mode + "'");
    }
}
