package de.helfenkannjeder.helfomat.core.organisation;

import de.helfenkannjeder.helfomat.core.geopoint.BoundingBox;
import de.helfenkannjeder.helfomat.core.geopoint.GeoPoint;

import java.util.List;

/**
 * @author Valentin Zickner
 */
public interface OrganisationRepository {

    boolean existsOrganisationWithSameTypeInDistance(String index, Organisation organisation, Long distanceInMeters);

    Organisation findByUrlName(String urlName);

    Organisation findOne(String id);

    List<ScoredOrganisation> findOrganisationsByQuestionAnswersAndDistanceSortByAnswerMatchAndDistance(
        List<QuestionAnswer> questionAnswers, GeoPoint position, double distance
    );

    List<ScoredOrganisation> findGlobalOrganisationsByQuestionAnswersSortByAnswerMatch(
        List<QuestionAnswer> questionAnswers)
        ;

    List<GeoPoint> findGeoPointsOfOrganisationsInsideBoundingBox(GeoPoint position, double distance, BoundingBox boundingBox);

    void save(String index, List<? extends Organisation> organisations);

    void createIndex(String index, String mapping);

    void deleteIndex(String index);

    void updateAlias(String index);

}
