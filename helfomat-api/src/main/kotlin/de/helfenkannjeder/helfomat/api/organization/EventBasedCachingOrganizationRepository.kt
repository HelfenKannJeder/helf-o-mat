package de.helfenkannjeder.helfomat.api.organization

import de.helfenkannjeder.helfomat.core.geopoint.BoundingBox
import de.helfenkannjeder.helfomat.core.geopoint.GeoPoint
import de.helfenkannjeder.helfomat.core.organization.*
import org.slf4j.LoggerFactory

/**
 * @author Valentin Zickner
 */
open class EventBasedCachingOrganizationRepository(
    protected val persistentOrganizationRepository: OrganizationRepository
) : OrganizationRepository {

    @JvmField
    protected val organizationBuilderMap: MutableMap<OrganizationId, Organization.Builder> = mutableMapOf()

    protected fun updateOrganizationBasedOnAllEvents(organizationId: OrganizationId) {
        LOG.debug("Update organization based on database events '{}'", organizationId)
        val organizationBuilder = buildOrganization(organizationId)
        saveOrganizationBuilder(organizationBuilder, organizationId)
    }

    private fun saveOrganizationBuilder(organizationBuilder: Organization.Builder?, organizationId: OrganizationId) {
        if (organizationBuilder == null) {
            organizationBuilderMap.remove(organizationId)
            persistentOrganizationRepository.remove(organizationId)
        } else {
            saveToLocalCache(organizationId, organizationBuilder)
            persistentOrganizationRepository.save(listOf(organizationBuilder.build()))
        }
    }

    protected open fun buildOrganization(organizationId: OrganizationId): Organization.Builder? {
        return organizationBuilderMap.get(organizationId)
    }

    protected open fun saveToLocalCache(organizationId: OrganizationId, organizationBuilder: Organization.Builder) {
        organizationBuilderMap[organizationId] = organizationBuilder
    }

    override fun findOrganizationWithSameTypeInDistance(defaultAddress: Address?, organizationType: OrganizationType, distanceInMeters: Long): List<Organization> {
        return persistentOrganizationRepository.findOrganizationWithSameTypeInDistance(defaultAddress, organizationType, distanceInMeters)
    }

    override fun findByUrlName(urlName: String): Organization? {
        return persistentOrganizationRepository.findByUrlName(urlName)
    }

    override fun findOne(id: String): Organization? {
        return persistentOrganizationRepository.findOne(id)
    }

    override fun findOrganizationsByQuestionAnswersAndDistanceSortByAnswerMatchAndDistance(questionAnswers: List<QuestionAnswer>, position: GeoPoint, distance: Double): List<ScoredOrganization> {
        return persistentOrganizationRepository.findOrganizationsByQuestionAnswersAndDistanceSortByAnswerMatchAndDistance(questionAnswers, position, distance)
    }

    override fun findOrganizationsByDistanceSortByDistance(position: GeoPoint, distance: Double): List<Organization> {
        return persistentOrganizationRepository.findOrganizationsByDistanceSortByDistance(position, distance)
    }

    override fun findGlobalOrganizationsByQuestionAnswersSortByAnswerMatch(questionAnswers: List<QuestionAnswer>): List<ScoredOrganization> {
        return persistentOrganizationRepository.findGlobalOrganizationsByQuestionAnswersSortByAnswerMatch(questionAnswers)
    }

    override fun findGlobalOrganizations(): List<Organization> {
        return persistentOrganizationRepository.findGlobalOrganizations()
    }

    override fun findGeoPointsOfOrganizationsInsideBoundingBox(position: GeoPoint?, distance: Double, boundingBox: BoundingBox): List<GeoPoint> {
        return persistentOrganizationRepository.findGeoPointsOfOrganizationsInsideBoundingBox(position, distance, boundingBox)
    }

    override fun save(organizations: List<Organization>) {
        throw UnsupportedOperationException()
    }

    override fun remove(organizationId: OrganizationId) {
        throw UnsupportedOperationException()
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(EventBasedCachingOrganizationRepository::class.java)
    }

}