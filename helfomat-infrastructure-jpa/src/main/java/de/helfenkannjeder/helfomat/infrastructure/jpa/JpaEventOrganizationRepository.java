package de.helfenkannjeder.helfomat.infrastructure.jpa;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.helfenkannjeder.helfomat.api.organisation.EventBasedCachingOrganizationRepository;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationRepository;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEvent;
import org.springframework.context.event.EventListener;

import java.util.List;

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
        if (!organisationBuilderMap.containsKey(organisationId)) {
            List<Event> events = this.eventRepository.findByOrganizationId(organisationId);
            for (Event event : events) {
                super.processDomainEvent(event.getDomainEvent());
            }
        }
        super.processDomainEvent(organisationEvent);
    }
}
