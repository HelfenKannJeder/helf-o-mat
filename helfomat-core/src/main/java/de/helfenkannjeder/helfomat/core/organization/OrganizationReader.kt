package de.helfenkannjeder.helfomat.core.organization;

/**
 * @author Valentin Zickner
 */
public interface OrganizationReader {

    String getName();

    Organization read() throws Exception;

}
