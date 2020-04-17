package de.helfenkannjeder.helfomat.infrastructure.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.helfenkannjeder.helfomat.core.ProfileRegistry;
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Profile(ProfileRegistry.KAFKA)
public class DomainEventToKafkaListener {

    private final Logger LOG = LoggerFactory.getLogger(DomainEventToKafkaListener.class);

    private final KafkaTemplate<String, byte[]> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public DomainEventToKafkaListener(KafkaTemplate<String, byte[]> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @EventListener
    public void listen(OrganizationEvent organizationEvent) throws JsonProcessingException {
        LOG.debug("Received domain event {}", organizationEvent);
        byte[] bytes = this.objectMapper.writeValueAsBytes(organizationEvent);
        this.kafkaTemplate.sendDefault(organizationEvent.getOrganizationId().getValue(), bytes);
    }
}
