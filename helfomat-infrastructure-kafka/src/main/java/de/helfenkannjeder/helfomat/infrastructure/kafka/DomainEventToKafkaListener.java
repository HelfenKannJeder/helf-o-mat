package de.helfenkannjeder.helfomat.infrastructure.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class DomainEventToKafkaListener {

    private final Logger LOG = LoggerFactory.getLogger(DomainEventToKafkaListener.class);

    private final KafkaTemplate<String, byte[]> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public DomainEventToKafkaListener(KafkaTemplate<String, byte[]> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @EventListener
    public void listen(OrganisationEvent organisationEvent) throws JsonProcessingException {
        LOG.debug("Received domain event {}", organisationEvent);
        byte[] bytes = this.objectMapper.writeValueAsBytes(organisationEvent);
        this.kafkaTemplate.sendDefault(organisationEvent.getOrganisationId().getValue(), bytes);
    }
}
