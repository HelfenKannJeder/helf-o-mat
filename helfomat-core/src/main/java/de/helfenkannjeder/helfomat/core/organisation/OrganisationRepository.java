package de.helfenkannjeder.helfomat.core.organisation;

import de.helfenkannjeder.helfomat.core.geopoint.BoundingBox;
import de.helfenkannjeder.helfomat.core.geopoint.GeoPoint;
import de.helfenkannjeder.helfomat.core.question.Answer;

import java.util.List;
import java.util.Map;

/**
 * @author Valentin Zickner
 */
public interface OrganisationRepository {

    boolean existsOrganisationWithSameTypeInDistance(String index, Organisation organisation, Long distanceInMeters);

    Organisation findOne(String id);

    List<ScoredOrganisation> findOrganisations(Map<String, Answer> questionAnswers,
                                               GeoPoint position,
                                               double distance);

    List<ScoredOrganisation> findGlobalOrganisations(Map<String, Answer> questionAnswers);

    List<GeoPoint> findClusteredGeoPoints(GeoPoint position, double distance, BoundingBox boundingBox);

    void save(String index, List<? extends Organisation> organisations);

    void createIndex(String index, String mapping);

    void deleteIndex(String index);

    void updateAlias(String index);

}
