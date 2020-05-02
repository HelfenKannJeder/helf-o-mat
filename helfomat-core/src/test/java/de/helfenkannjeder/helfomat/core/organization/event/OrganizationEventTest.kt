package de.helfenkannjeder.helfomat.core.organization.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.helfenkannjeder.helfomat.core.organization.OrganizationId;
import de.helfenkannjeder.helfomat.core.organization.OrganizationType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrganizationEventTest {

    @Test
    void objectMapper_serializeOrganizationEvent_ensureDeserializationWorksAndContainsSameContent() throws Exception {
        // Arrange
        ObjectMapper objectMapper = new ObjectMapper();
        OrganizationCreateEvent organizationCreateEvent = new OrganizationCreateEvent(
            new OrganizationId(),
            "Test Organization",
            "test-organization",
            OrganizationType.THW
        );

        // Act
        byte[] bytes = objectMapper.writeValueAsBytes(organizationCreateEvent);
        OrganizationEvent result = objectMapper.readValue(bytes, OrganizationEvent.class);

        // Assert
        assertThat(result).isInstanceOf(OrganizationCreateEvent.class);
        assertThat(result.getOrganizationId()).isEqualTo(organizationCreateEvent.getOrganizationId());
        OrganizationCreateEvent event = (OrganizationCreateEvent) result;
        assertThat(event.getName()).isEqualTo(organizationCreateEvent.getName());
        assertThat(event.getUrlName()).isEqualTo(organizationCreateEvent.getUrlName());
        assertThat(event.getOrganizationType()).isEqualTo(organizationCreateEvent.getOrganizationType());
    }

}