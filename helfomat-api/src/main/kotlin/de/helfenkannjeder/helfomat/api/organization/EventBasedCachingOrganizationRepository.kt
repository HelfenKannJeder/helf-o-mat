package de.helfenkannjeder.helfomat.api.organization

import de.helfenkannjeder.helfomat.core.geopoint.BoundingBox
import de.helfenkannjeder.helfomat.core.geopoint.GeoPoint
import de.helfenkannjeder.helfomat.core.organization.*
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEvent
import org.slf4j.LoggerFactory

/**
 * @author Valentin Zickner
 */
open class EventBasedCachingOrganizationRepository(
    protected val persistentOrganizationRepository: OrganizationRepository
) : OrganizationRepository {

    @JvmField
    protected val organizationBuilderMap: MutableMap<OrganizationId, Organization.Builder> = mutableMapOf()

    protected fun processDomainEvents(organizationId: OrganizationId, organizationEvents: List<OrganizationEvent>) {
        LOG.debug("Received organization events for organization '{}' from event storage '{}'", organizationId, organizationEvents)
        val organizationBuilder = getExistingOrganizationBuilder(organizationId)
        organizationEvents.forEach { it.applyOnOrganizationBuilder(organizationBuilder) }
        saveToLocalCache(organizationId, organizationBuilder)
        persistentOrganizationRepository.save(listOf(organizationBuilder.build()))
    }

    protected open fun processDomainEvent(organizationEvent: OrganizationEvent) {
        val organizationId = organizationEvent.organizationId
        LOG.debug("Received organization event for organization '{}' from event storage '{}'", organizationId, organizationEvent)
        val organizationBuilder = organizationEvent.applyOnOrganizationBuilder(
            getExistingOrganizationBuilder(organizationId)
        )
        saveToLocalCache(organizationId, organizationBuilder)
        persistentOrganizationRepository.save(listOf(organizationBuilder.build()))
    }

    protected open fun getExistingOrganizationBuilder(organizationId: OrganizationId): Organization.Builder {
        return organizationBuilderMap.getOrDefault(organizationId, Organization.Builder())
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

    companion object {
        private val LOG = LoggerFactory.getLogger(EventBasedCachingOrganizationRepository::class.java)
    }

}