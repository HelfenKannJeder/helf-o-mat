package de.helfenkannjeder.helfomat.core.organisation;

import de.helfenkannjeder.helfomat.core.geopoint.BoundingBox;
import de.helfenkannjeder.helfomat.core.geopoint.GeoPoint;

import java.util.List;

/**
 * @author Valentin Zickner
 */
public interface OrganisationRepository {

    boolean existsOrganisationWithSameTypeInDistance(Organisation organisation, Long distanceInMeters);

    Organisation findByUrlName(String urlName);

    Organisation findOne(String id);

    List<ScoredOrganisation> findOrganisationsByQuestionAnswersAndDistanceSortByAnswerMatchAndDistance(List<QuestionAnswer> questionAnswers, GeoPoint position, double distance);

    List<Organisation> findOrganisationsByDistanceSortByDistance(GeoPoint position, double distance);

    List<ScoredOrganisation> findGlobalOrganisationsByQuestionAnswersSortByAnswerMatch(List<QuestionAnswer> questionAnswers);

    List<Organisation> findGlobalOrganisations();

    List<GeoPoint> findGeoPointsOfOrganisationsInsideBoundingBox(GeoPoint position, double distance, BoundingBox boundingBox);

    void save(List<? extends Organisation> organisations);

    void createIndex(String mapping);

    void deleteIndex();

    void updateAlias(String alias);
}
