package de.helfenkannjeder.helfomat.core.organisation;

/**
 * @author Valentin Zickner
 */
public interface OrganisationReader {

    String getName();

    Organisation read() throws Exception;

}
