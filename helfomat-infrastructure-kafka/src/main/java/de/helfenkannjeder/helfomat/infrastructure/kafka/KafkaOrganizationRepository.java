package de.helfenkannjeder.helfomat.infrastructure.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.helfenkannjeder.helfomat.api.organization.EventBasedCachingOrganizationRepository;
import de.helfenkannjeder.helfomat.core.organization.OrganizationRepository;
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEvent;
import org.springframework.kafka.annotation.KafkaListener;

import java.io.IOException;

/**
 * @author Valentin Zickner
 */
public class KafkaOrganizationRepository extends EventBasedCachingOrganizationRepository implements OrganizationRepository {

    public KafkaOrganizationRepository(ObjectMapper objectMapper, OrganizationRepository persistentOrganizationRepository) {
        super(objectMapper, persistentOrganizationRepository);
    }

    @KafkaListener(topics = "${kafka.topic.organization-events}")
    public void listen(byte[] organizationEventByteArray) throws IOException {
        OrganizationEvent organizationEvent = this.objectMapper.readValue(organizationEventByteArray, OrganizationEvent.class);
        processDomainEvent(organizationEvent);
    }

}
