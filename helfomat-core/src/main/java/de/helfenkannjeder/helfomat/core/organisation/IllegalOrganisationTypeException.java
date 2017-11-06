package de.helfenkannjeder.helfomat.core.organisation;

/**
 * @author Valentin Zickner
 */
public class IllegalOrganisationTypeException extends RuntimeException {

    public IllegalOrganisationTypeException(String invalidName) {
        super("Unable to find organisation type " + invalidName);
    }
}
