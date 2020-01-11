package de.helfenkannjeder.helfomat.core.organisation.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationType;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OrganisationEventTest {

    @Test
    public void objectMapper_serializeOrganisationEvent_ensureDeserializationWorksAndContainsSameContent() throws Exception {
        // Arrange
        ObjectMapper objectMapper = new ObjectMapper();
        OrganisationCreateEvent organisationCreateEvent = new OrganisationCreateEvent(
            new OrganisationId(),
            "Test Organisation",
            "test-organisation",
            OrganisationType.THW
        );

        // Act
        byte[] bytes = objectMapper.writeValueAsBytes(organisationCreateEvent);
        OrganisationEvent result = objectMapper.readValue(bytes, OrganisationEvent.class);

        // Assert
        assertThat(result).isInstanceOf(OrganisationCreateEvent.class);
        assertThat(result.getOrganisationId()).isEqualTo(organisationCreateEvent.getOrganisationId());
        OrganisationCreateEvent event = (OrganisationCreateEvent) result;
        assertThat(event.getName()).isEqualTo(organisationCreateEvent.getName());
        assertThat(event.getUrlName()).isEqualTo(organisationCreateEvent.getUrlName());
        assertThat(event.getOrganisationType()).isEqualTo(organisationCreateEvent.getOrganisationType());
    }

}