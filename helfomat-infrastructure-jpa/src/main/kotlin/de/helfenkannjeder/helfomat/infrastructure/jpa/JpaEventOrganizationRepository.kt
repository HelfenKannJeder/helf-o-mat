package de.helfenkannjeder.helfomat.infrastructure.jpa

import de.helfenkannjeder.helfomat.api.organization.EventBasedCachingOrganizationRepository
import de.helfenkannjeder.helfomat.core.organization.Organization
import de.helfenkannjeder.helfomat.core.organization.OrganizationId
import de.helfenkannjeder.helfomat.core.organization.OrganizationRepository
import de.helfenkannjeder.helfomat.core.organization.event.ReloadOrganizationEvent
import org.springframework.context.event.EventListener

/**
 * @author Valentin Zickner
 */
open class JpaEventOrganizationRepository(
    persistentOrganizationRepository: OrganizationRepository,
    private val eventRepository: EventRepository
) : EventBasedCachingOrganizationRepository(persistentOrganizationRepository) {

    @EventListener
    open fun listen(reloadOrganizationEvent: ReloadOrganizationEvent) {
        super.updateOrganizationBasedOnAllEvents(reloadOrganizationEvent.organizationId)
    }

    override fun buildOrganization(organizationId: OrganizationId): Organization.Builder? {
        val organizationBuilder = organizationBuilderMap[organizationId]
        if (organizationBuilder != null) {
            return organizationBuilder
        }
        var newOrganizationBuilder: Organization.Builder? = null
        eventRepository.findByOrganizationId(organizationId)
            .map { it.domainEvent }
            .forEach { newOrganizationBuilder = it.applyOnOrganizationBuilder(newOrganizationBuilder) }
        return newOrganizationBuilder
    }

    override fun saveToLocalCache(organizationId: OrganizationId, organizationBuilder: Organization.Builder) {}

}