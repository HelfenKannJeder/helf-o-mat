package de.helfenkannjeder.helfomat.infrastructure.jpa

import de.helfenkannjeder.helfomat.api.organization.EventBasedCachingOrganizationRepository
import de.helfenkannjeder.helfomat.core.organization.Organization
import de.helfenkannjeder.helfomat.core.organization.OrganizationId
import de.helfenkannjeder.helfomat.core.organization.OrganizationRepository
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEvent
import org.springframework.context.event.EventListener

/**
 * @author Valentin Zickner
 */
class JpaEventOrganizationRepository(
    persistentOrganizationRepository: OrganizationRepository,
    private val eventRepository: EventRepository
) : EventBasedCachingOrganizationRepository(persistentOrganizationRepository) {

    @EventListener
    fun listen(organizationEvent: OrganizationEvent) {
        processDomainEvent(organizationEvent)
    }

    override fun processDomainEvent(organizationEvent: OrganizationEvent) {
        val organizationId = organizationEvent.organizationId
        super.processDomainEvents(organizationId, listOf(organizationEvent))
    }

    override fun getExistingOrganizationBuilder(organizationId: OrganizationId): Organization.Builder {
        val organizationBuilder = organizationBuilderMap[organizationId]
        if (organizationBuilder != null) {
            return organizationBuilder
        }
        val newOrganizationBuilder = Organization.Builder()
        eventRepository.findByOrganizationId(organizationId)
            .map { it.domainEvent }
            .forEach { it.applyOnOrganizationBuilder(newOrganizationBuilder) }
        return newOrganizationBuilder
    }

    override fun saveToLocalCache(organizationId: OrganizationId, organizationBuilder: Organization.Builder) {}

}