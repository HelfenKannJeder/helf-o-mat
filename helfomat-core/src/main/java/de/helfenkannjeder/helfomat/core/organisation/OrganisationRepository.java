package de.helfenkannjeder.helfomat.core.organisation;

/**
 * @author Valentin Zickner
 */
public interface OrganisationRepository {

    boolean existsOrganisationWithSameTypeInDistance(String index, Organisation organisation, Long distanceInMeters);

    Organisation findOne(String id);

}
