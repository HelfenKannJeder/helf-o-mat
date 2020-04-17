package de.helfenkannjeder.helfomat.core.organization;

/**
 * @author Valentin Zickner
 */
@SuppressWarnings("WeakerAccess")
public class IllegalOrganizationTypeException extends RuntimeException {

    public IllegalOrganizationTypeException(String invalidName) {
        super("Unable to find organization type " + invalidName);
    }
}
