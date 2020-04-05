package de.helfenkannjeder.helfomat.infrastructure.jpa;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.helfenkannjeder.helfomat.api.organisation.EventBasedCachingOrganizationRepository;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationRepository;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEvent;
import org.springframework.context.event.EventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Valentin Zickner
 */
public class JpaEventOrganizationRepository extends EventBasedCachingOrganizationRepository {

    private EventRepository eventRepository;

    public JpaEventOrganizationRepository(ObjectMapper objectMapper, OrganisationRepository persistentOrganisationRepository, EventRepository eventRepository) {
        super(objectMapper, persistentOrganisationRepository);
        this.eventRepository = eventRepository;
    }

    @EventListener
    public void listen(OrganisationEvent organisationEvent) {
        processDomainEvent(organisationEvent);
    }

    @Override
    protected void processDomainEvent(OrganisationEvent organisationEvent) {
        OrganisationId organisationId = organisationEvent.getOrganisationId();
        List<OrganisationEvent> events = new ArrayList<>();
        if (!organisationBuilderMap.containsKey(organisationId)) {
            events = this.eventRepository.findByOrganizationId(organisationId)
                .stream()
                .map(Event::getDomainEvent)
                .collect(Collectors.toList());
        }
        events.add(organisationEvent);
        super.processDomainEvents(organisationId, events);
    }
}
