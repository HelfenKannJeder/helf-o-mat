package de.helfenkannjeder.helfomat.rest

import de.helfenkannjeder.helfomat.api.organization.OrganizationDetailDto
import de.helfenkannjeder.helfomat.api.organization.SearchSimilarOrganizationDto
import de.helfenkannjeder.helfomat.api.organization.toAddressDto
import de.helfenkannjeder.helfomat.api.organization.toOrganizations
import de.helfenkannjeder.helfomat.config.ImporterConfiguration
import de.helfenkannjeder.helfomat.core.geopoint.BoundingBox
import de.helfenkannjeder.helfomat.core.geopoint.GeoPoint
import de.helfenkannjeder.helfomat.core.organization.*
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange

/**
 * @author Valentin Zickner
 */
@Component
class RestOrganizationRepository(
    private val restTemplate: RestTemplate,
    private val importerConfiguration: ImporterConfiguration
) : OrganizationRepository {

    override fun findOrganizationWithSameTypeInDistance(defaultAddress: Address?, organizationType: OrganizationType, distanceInMeters: Long): List<Organization> {
        val searchSimilarOrganizationDto = SearchSimilarOrganizationDto(
            defaultAddress?.toAddressDto(),
            organizationType,
            distanceInMeters
        )
        val requestEntity = HttpEntity(searchSimilarOrganizationDto, HttpHeaders())
        val result: ResponseEntity<List<OrganizationDetailDto>> = restTemplate.exchange(importerConfiguration.webApiUrl + "/api/organization/search-similar", HttpMethod.POST, requestEntity)
        return result.body?.toOrganizations() ?: emptyList()
    }

    override fun findByUrlName(urlName: String): Organization? {
        throw UnsupportedOperationException()
    }

    override fun findOne(id: String): Organization? {
        throw UnsupportedOperationException()
    }

    override fun findOrganizationsByQuestionAnswersAndDistanceSortByAnswerMatchAndDistance(questionAnswers: List<QuestionAnswer>, position: GeoPoint, distance: Double): List<ScoredOrganization> {
        throw UnsupportedOperationException()
    }

    override fun findOrganizationsByDistanceSortByDistance(position: GeoPoint, distance: Double): List<Organization> {
        throw UnsupportedOperationException()
    }

    override fun findGlobalOrganizationsByQuestionAnswersSortByAnswerMatch(questionAnswers: List<QuestionAnswer>): List<ScoredOrganization> {
        throw UnsupportedOperationException()
    }

    override fun findGlobalOrganizations(): List<Organization> {
        throw UnsupportedOperationException()
    }

    override fun findGeoPointsOfOrganizationsInsideBoundingBox(position: GeoPoint?, distance: Double, boundingBox: BoundingBox): List<GeoPoint> {
        throw UnsupportedOperationException()
    }

    override fun save(organizations: List<Organization>) {
        throw UnsupportedOperationException()
    }

    override fun remove(organizationId: OrganizationId) {
        throw UnsupportedOperationException()
    }

}