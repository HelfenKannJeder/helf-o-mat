package de.helfenkannjeder.helfomat.infrastructure.jpa

import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEvent
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

/**
 * @author Valentin Zickner
 */
@Component
class JpaDomainEventListener(
    private val eventRepository: EventRepository
) {

    @EventListener
    fun listen(organizationEvent: OrganizationEvent) {
        LOG.debug("Received domain event {}", organizationEvent)
        eventRepository.save(
            Event(EventId(), organizationEvent.organizationId, organizationEvent)
        )
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(JpaDomainEventListener::class.java)
    }

}