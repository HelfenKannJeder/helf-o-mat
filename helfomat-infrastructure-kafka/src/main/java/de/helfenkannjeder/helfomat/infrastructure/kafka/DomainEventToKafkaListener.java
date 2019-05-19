package de.helfenkannjeder.helfomat.infrastructure.kafka;

import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class DomainEventToKafkaListener {

    private final Logger LOG = LoggerFactory.getLogger(DomainEventToKafkaListener.class);

    private final KafkaTemplate<OrganisationId, OrganisationEvent> kafkaTemplate;

    public DomainEventToKafkaListener(KafkaTemplate<OrganisationId, OrganisationEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @EventListener
    public void listen(OrganisationEvent organisationEvent) {
        LOG.debug("Received domain event {}", organisationEvent);
        this.kafkaTemplate.sendDefault(organisationEvent.getOrganisationId(), organisationEvent);
    }
}
