package de.helfenkannjeder.helfomat.infrastructure.jpa;

import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEvent;
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
    public void listen(OrganisationEvent organisationEvent) {
        LOG.debug("Received domain event {}", organisationEvent);
        this.eventRepository.save(
            new Event(
                new EventId(),
                organisationEvent
            )
        );
    }

}
