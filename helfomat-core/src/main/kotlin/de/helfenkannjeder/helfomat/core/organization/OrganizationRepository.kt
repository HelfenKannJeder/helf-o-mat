package de.helfenkannjeder.helfomat.core.organization;

import de.helfenkannjeder.helfomat.core.geopoint.BoundingBox;
import de.helfenkannjeder.helfomat.core.geopoint.GeoPoint;

import java.util.List;

/**
 * @author Valentin Zickner
 */
public interface OrganizationRepository {

    List<Organization> findOrganizationWithSameTypeInDistance(Address defaultAddress, OrganizationType organizationType, Long distanceInMeters);

    Organization findByUrlName(String urlName);

    Organization findOne(String id);

    List<ScoredOrganization> findOrganizationsByQuestionAnswersAndDistanceSortByAnswerMatchAndDistance(List<QuestionAnswer> questionAnswers, GeoPoint position, double distance);

    List<Organization> findOrganizationsByDistanceSortByDistance(GeoPoint position, double distance);

    List<ScoredOrganization> findGlobalOrganizationsByQuestionAnswersSortByAnswerMatch(List<QuestionAnswer> questionAnswers);

    List<Organization> findGlobalOrganizations();

    List<GeoPoint> findGeoPointsOfOrganizationsInsideBoundingBox(GeoPoint position, double distance, BoundingBox boundingBox);

    void save(List<? extends Organization> organizations);

}
