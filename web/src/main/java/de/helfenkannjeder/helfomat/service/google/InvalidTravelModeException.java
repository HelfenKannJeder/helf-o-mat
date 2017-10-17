package de.helfenkannjeder.helfomat.service.google;

/**
 * @author Valentin Zickner
 */
public class InvalidTravelModeException extends RuntimeException {
    InvalidTravelModeException(String mode) {
        super("Invalid travel mode '" + mode + "'");
    }
}
