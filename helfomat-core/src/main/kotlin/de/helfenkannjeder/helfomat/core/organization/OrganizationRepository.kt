package de.helfenkannjeder.helfomat.core.organization

import de.helfenkannjeder.helfomat.core.geopoint.BoundingBox
import de.helfenkannjeder.helfomat.core.geopoint.GeoPoint

/**
 * @author Valentin Zickner
 */
interface OrganizationRepository {
    fun findOrganizationWithSameTypeInDistance(defaultAddress: Address?, organizationType: OrganizationType, distanceInMeters: Long): List<Organization>
    fun findByUrlName(urlName: String): Organization?
    fun findOne(id: String): Organization?
    fun findOrganizationsByQuestionAnswersAndDistanceSortByAnswerMatchAndDistance(questionAnswers: List<QuestionAnswer>, position: GeoPoint, distance: Double): List<ScoredOrganization>
    fun findOrganizationsByDistanceSortByDistance(position: GeoPoint, distance: Double): List<Organization>
    fun findGlobalOrganizationsByQuestionAnswersSortByAnswerMatch(questionAnswers: List<QuestionAnswer>): List<ScoredOrganization>
    fun findGlobalOrganizations(): List<Organization>
    fun findGeoPointsOfOrganizationsInsideBoundingBox(position: GeoPoint?, distance: Double, boundingBox: BoundingBox): List<GeoPoint>
    fun save(organizations: List<Organization>)
    fun remove(organizationId: OrganizationId)
}