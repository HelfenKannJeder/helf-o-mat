package de.helfenkannjeder.helfomat.core.organization.event

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import de.helfenkannjeder.helfomat.core.organization.OrganizationId
import de.helfenkannjeder.helfomat.core.organization.OrganizationType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class OrganizationEventTest {

    @Test
    fun objectMapper_serializeOrganizationEvent_ensureDeserializationWorksAndContainsSameContent() {
        // Arrange
        val objectMapper = ObjectMapper()
        objectMapper.registerKotlinModule()
        val organizationCreateEvent = OrganizationCreateEvent(
            OrganizationId(),
            "Test Organization",
            "test-organization",
            OrganizationType.THW
        )

        // Act
        val bytes = objectMapper.writeValueAsBytes(organizationCreateEvent)
        val result = objectMapper.readValue(bytes, OrganizationEvent::class.java)

        // Assert
        assertThat(result).isInstanceOf(OrganizationCreateEvent::class.java)
        assertThat(result.organizationId).isEqualTo(organizationCreateEvent.organizationId)
        val (_, name, urlName, organizationType) = result as OrganizationCreateEvent
        assertThat(name).isEqualTo(organizationCreateEvent.name)
        assertThat(urlName).isEqualTo(organizationCreateEvent.urlName)
        assertThat(organizationType).isEqualTo(organizationCreateEvent.organizationType)
    }
}