package de.helfenkannjeder.helfomat.infrastructure.jpa;

import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author Valentin Zickner
 */
@Component
public class JpaDomainEventListener {

    private final static Logger LOG = LoggerFactory.getLogger(JpaDomainEventListener.class);

    private final EventRepository eventRepository;

    public JpaDomainEventListener(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @EventListener
    public void listen(OrganizationEvent organizationEvent) {
        LOG.debug("Received domain event {}", organizationEvent);
        this.eventRepository.save(
            new Event(
                new EventId(),
                organizationEvent
            )
        );
    }

}
