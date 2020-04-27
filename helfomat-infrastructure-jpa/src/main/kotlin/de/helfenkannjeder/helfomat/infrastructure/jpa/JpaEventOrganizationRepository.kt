package de.helfenkannjeder.helfomat.infrastructure.jpa;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.helfenkannjeder.helfomat.api.organization.EventBasedCachingOrganizationRepository;
import de.helfenkannjeder.helfomat.core.organization.Organization;
import de.helfenkannjeder.helfomat.core.organization.OrganizationId;
import de.helfenkannjeder.helfomat.core.organization.OrganizationRepository;
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEvent;
import org.springframework.context.event.EventListener;

import java.util.Collections;

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
        super.processDomainEvents(organizationId, Collections.singletonList(organizationEvent));
    }

    @Override
    protected Organization.Builder getExistingOrganizationBuilder(OrganizationId organizationId) {
        Organization.Builder organizationBuilder = this.organizationBuilderMap.get(organizationId);
        if (organizationBuilder != null) {
            return organizationBuilder;
        }
        Organization.Builder newOrganizationBuilder = new Organization.Builder();
        this.eventRepository.findByOrganizationId(organizationId)
            .stream()
            .map(Event::getDomainEvent)
            .forEach(domainEvent -> domainEvent.applyOnOrganizationBuilder(newOrganizationBuilder));
        return newOrganizationBuilder;
    }

    @Override
    protected void saveToLocalCache(OrganizationId organizationId, Organization.Builder organizationBuilder) {
    }
}
