package de.helfenkannjeder.helfomat.core.organisation;

import de.helfenkannjeder.helfomat.core.geopoint.BoundingBox;
import de.helfenkannjeder.helfomat.core.geopoint.GeoPoint;
import de.helfenkannjeder.helfomat.core.question.Answer;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Valentin Zickner
 */
public interface OrganisationRepository {

    boolean existsOrganisationWithSameTypeInDistance(String index, Organisation organisation, Long distanceInMeters);

    Organisation findOne(String id);

    LinkedHashMap<Organisation, Float> findOrganisations(Map<String, Answer> questionAnswers,
                                                         GeoPoint position,
                                                         double distance);

    LinkedHashMap<Organisation, Float> findGlobalOrganisations(Map<String, Answer> questionAnswers);

    List<GeoPoint> findClusteredGeoPoints(GeoPoint position, double distance, BoundingBox boundingBox);

    void save(String index, List<? extends Organisation> organisations);

    void createIndex(String index, String mapping);

    void updateAlias(String index);

}
