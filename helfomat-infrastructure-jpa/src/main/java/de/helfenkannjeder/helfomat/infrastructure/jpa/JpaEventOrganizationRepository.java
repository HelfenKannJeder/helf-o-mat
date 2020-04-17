package de.helfenkannjeder.helfomat.infrastructure.jpa;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.helfenkannjeder.helfomat.api.organization.EventBasedCachingOrganizationRepository;
import de.helfenkannjeder.helfomat.core.organization.OrganizationId;
import de.helfenkannjeder.helfomat.core.organization.OrganizationRepository;
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEvent;
import org.springframework.context.event.EventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Valentin Zickner
 */
public class JpaEventOrganizationRepository extends EventBasedCachingOrganizationRepository {

    private EventRepository eventRepository;

    public JpaEventOrganizationRepository(ObjectMapper objectMapper, OrganizationRepository persistentOrganizationRepository, EventRepository eventRepository) {
        super(objectMapper, persistentOrganizationRepository);
        this.eventRepository = eventRepository;
    }

    @EventListener
    public void listen(OrganizationEvent organizationEvent) {
        processDomainEvent(organizationEvent);
    }

    @Override
    protected void processDomainEvent(OrganizationEvent organizationEvent) {
        OrganizationId organizationId = organizationEvent.getOrganizationId();
        List<OrganizationEvent> events = new ArrayList<>();
        if (!organizationBuilderMap.containsKey(organizationId)) {
            events = this.eventRepository.findByOrganizationId(organizationId)
                .stream()
                .map(Event::getDomainEvent)
                .collect(Collectors.toList());
        }
        events.add(organizationEvent);
        super.processDomainEvents(organizationId, events);
    }
}
