package de.helfenkannjeder.helfomat.infrastructure.jpa;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.helfenkannjeder.helfomat.api.organisation.EventBasedCachingOrganizationRepository;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationRepository;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEvent;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import java.util.List;

/**
 * @author Valentin Zickner
 */
public class JpaEventOrganizationRepository extends EventBasedCachingOrganizationRepository implements ApplicationContextAware, ApplicationListener<ContextRefreshedEvent> {

    private ApplicationContext applicationContext;
    private EventRepository eventRepository;

    public JpaEventOrganizationRepository(ObjectMapper objectMapper, OrganisationRepository persistentOrganisationRepository, EventRepository eventRepository) {
        super(objectMapper, persistentOrganisationRepository);
        this.eventRepository = eventRepository;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        // run it only for the main application context event
        if (contextRefreshedEvent.getApplicationContext().equals(applicationContext)) {
            List<Event> events = this.eventRepository.findAll();
            for (Event event : events) {
                processDomainEvent(event.getDomainEvent());
            }
        }
    }

    @EventListener
    public void listen(OrganisationEvent organisationEvent) {
        processDomainEvent(organisationEvent);
    }

}
