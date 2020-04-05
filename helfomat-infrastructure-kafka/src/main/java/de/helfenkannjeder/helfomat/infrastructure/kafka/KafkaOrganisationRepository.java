package de.helfenkannjeder.helfomat.infrastructure.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.helfenkannjeder.helfomat.api.organisation.EventBasedCachingOrganizationRepository;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationRepository;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEvent;
import org.springframework.kafka.annotation.KafkaListener;

import java.io.IOException;

/**
 * @author Valentin Zickner
 */
public class KafkaOrganisationRepository extends EventBasedCachingOrganizationRepository implements OrganisationRepository {

    public KafkaOrganisationRepository(ObjectMapper objectMapper, OrganisationRepository persistentOrganisationRepository) {
        super(objectMapper, persistentOrganisationRepository);
    }

    @KafkaListener(topics = "${kafka.topic.organization-events}")
    public void listen(byte[] organisationEventByteArray) throws IOException {
        OrganisationEvent organisationEvent = this.objectMapper.readValue(organisationEventByteArray, OrganisationEvent.class);
        processDomainEvent(organisationEvent);
    }

}
