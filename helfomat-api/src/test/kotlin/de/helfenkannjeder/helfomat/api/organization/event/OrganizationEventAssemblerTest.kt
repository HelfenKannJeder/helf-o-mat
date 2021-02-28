package de.helfenkannjeder.helfomat.api.organization.event

import de.helfenkannjeder.helfomat.core.organization.Group
import de.helfenkannjeder.helfomat.core.organization.OrganizationTestDataFactory
import de.helfenkannjeder.helfomat.core.organization.OrganizationTestDataFactory.ORGANIZATION_1_BERGRUNG_1
import de.helfenkannjeder.helfomat.core.organization.OrganizationTestDataFactory.ORGANIZATION_1_OV_STAB
import de.helfenkannjeder.helfomat.core.organization.event.ConfirmedChangeOrganizationEvent
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEditChangeGroupEvent
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEditDeleteGroupEvent
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class OrganizationEventAssemblerTest {

    @Test
    fun testEventApplicable_withNotApplicableEvent_ensureFlagIsFalse() {
        // Arrange
        val organization = OrganizationTestDataFactory.ORGANIZATION_1
        val deleteEvent = OrganizationEditDeleteGroupEvent(
            organizationId = organization.id,
            group = ORGANIZATION_1_OV_STAB.copy(description = "Wrong description")
        )

        // Act
        val event = deleteEvent.toOrganizationEventDto(emptyList(), organization)

        // Assert
        assertThat(event).isInstanceOf(OrganizationEditDeleteGroupEventDto::class.java)
        val organizationEditDeleteGroupEventDto = event as OrganizationEditDeleteGroupEventDto
        assertThat(organizationEditDeleteGroupEventDto.eventApplicable).isEqualTo(EventApplicability.NONE)
    }

    @Test
    fun testEventApplicable_withEventApplicable_ensureFlagIsTrue() {
        // Arrange
        val organization = OrganizationTestDataFactory.ORGANIZATION_1
        val deleteEvent = OrganizationEditDeleteGroupEvent(
            organizationId = organization.id,
            group = ORGANIZATION_1_OV_STAB.copy()
        )

        // Act
        val event = deleteEvent.toOrganizationEventDto(emptyList(), organization)

        // Assert
        assertThat(event).isInstanceOf(OrganizationEditDeleteGroupEventDto::class.java)
        val organizationEditDeleteGroupEventDto = event as OrganizationEditDeleteGroupEventDto
        assertThat(organizationEditDeleteGroupEventDto.eventApplicable).isEqualTo(EventApplicability.FULL)
    }

    @Test
    fun testEventApplicable_withBoxedNotApplicableEvent_ensureFlagIsFalse() {
        // Arrange
        val organization = OrganizationTestDataFactory.ORGANIZATION_1
        val deleteEvent = OrganizationEditDeleteGroupEvent(
            organizationId = organization.id,
            group = ORGANIZATION_1_OV_STAB.copy(description = "Wrong description")
        )
        val boxedEvent = ConfirmedChangeOrganizationEvent(
            organizationId = organization.id,
            approvedBy = "thomas",
            author = "thomas",
            sources = "test",
            changes = listOf(deleteEvent)
        )

        // Act
        val event = boxedEvent.toOrganizationEventDto(emptyList(), organization)

        // Assert
        assertThat(event).isInstanceOf(ConfirmedChangeOrganizationEventDto::class.java)
        val confirmedChangeOrganizationEvent = event as ConfirmedChangeOrganizationEventDto
        assertThat(confirmedChangeOrganizationEvent.changes).hasSize(1)
        val detailEvent = confirmedChangeOrganizationEvent.changes[0]
        assertThat(detailEvent).isInstanceOf(OrganizationEditDeleteGroupEventDto::class.java)
        val organizationEditDeleteGroupEventDto = detailEvent as OrganizationEditDeleteGroupEventDto
        assertThat(organizationEditDeleteGroupEventDto.eventApplicable).isEqualTo(EventApplicability.NONE)
    }

    @Test
    fun testEventApplicable_withBoxedEventApplicable_ensureFlagIsTrue() {
        // Arrange
        val organization = OrganizationTestDataFactory.ORGANIZATION_1
        val deleteEvent = OrganizationEditDeleteGroupEvent(
            organizationId = organization.id,
            group = ORGANIZATION_1_OV_STAB.copy()
        )
        val boxedEvent = ConfirmedChangeOrganizationEvent(
            organizationId = organization.id,
            approvedBy = "thomas",
            author = "thomas",
            sources = "test",
            changes = listOf(deleteEvent)
        )

        // Act
        val event = boxedEvent.toOrganizationEventDto(emptyList(), organization)

        // Assert
        assertThat(event).isInstanceOf(ConfirmedChangeOrganizationEventDto::class.java)
        val confirmedChangeOrganizationEvent = event as ConfirmedChangeOrganizationEventDto
        assertThat(confirmedChangeOrganizationEvent.changes).hasSize(1)
        val detailEvent = confirmedChangeOrganizationEvent.changes[0]
        assertThat(detailEvent).isInstanceOf(OrganizationEditDeleteGroupEventDto::class.java)
        val organizationEditDeleteGroupEventDto = detailEvent as OrganizationEditDeleteGroupEventDto
        assertThat(organizationEditDeleteGroupEventDto.eventApplicable).isEqualTo(EventApplicability.FULL)
    }

    @Test
    fun testEventApplicable_withBoxedEventApplicableAndNotApplicable_ensureFlagIsTrueAndFalse() {
        // Arrange
        val organization = OrganizationTestDataFactory.ORGANIZATION_1
        val deleteEvent = OrganizationEditDeleteGroupEvent(
            organizationId = organization.id,
            group = ORGANIZATION_1_OV_STAB.copy()
        )
        val deleteEvent2 = OrganizationEditDeleteGroupEvent(
            organizationId = organization.id,
            group = ORGANIZATION_1_OV_STAB.copy()
        )
        val boxedEvent = ConfirmedChangeOrganizationEvent(
            organizationId = organization.id,
            approvedBy = "thomas",
            author = "thomas",
            sources = "test",
            changes = listOf(deleteEvent, deleteEvent2)
        )

        // Act
        val event = boxedEvent.toOrganizationEventDto(emptyList(), organization)

        // Assert
        assertThat(event).isInstanceOf(ConfirmedChangeOrganizationEventDto::class.java)
        val confirmedChangeOrganizationEvent = event as ConfirmedChangeOrganizationEventDto
        assertThat(confirmedChangeOrganizationEvent.changes).hasSize(2)
        val detailEvent1 = confirmedChangeOrganizationEvent.changes[0]
        assertThat(detailEvent1).isInstanceOf(OrganizationEditDeleteGroupEventDto::class.java)
        val organizationEditDeleteGroupEventDto1 = detailEvent1 as OrganizationEditDeleteGroupEventDto
        assertThat(organizationEditDeleteGroupEventDto1.eventApplicable).isEqualTo(EventApplicability.FULL)
        val detailEvent2 = confirmedChangeOrganizationEvent.changes[1]
        assertThat(detailEvent2).isInstanceOf(OrganizationEditDeleteGroupEventDto::class.java)
        val organizationEditDeleteGroupEventDto2 = detailEvent2 as OrganizationEditDeleteGroupEventDto
        assertThat(organizationEditDeleteGroupEventDto2.eventApplicable).isEqualTo(EventApplicability.NONE)
    }

    @Test
    fun testEventApplicable_withSourceMismatchEventApplicable_ensureFlagIsSetToPartial() {
        // Arrange
        val organization = OrganizationTestDataFactory.ORGANIZATION_1
        val changeOrganization = OrganizationEditChangeGroupEvent(
            organizationId = organization.id,
            indexOffset = 0,
            oldGroup = Group(ORGANIZATION_1_BERGRUNG_1.name, description = null),
            group = Group("Bergungsgruppe", description = "Bergungsgruppe ersetzt BG1")
        )

        // Act
        val event = changeOrganization.toOrganizationEventDto(emptyList(), organization)

        // Assert
        assertThat(event).isInstanceOf(OrganizationEditChangeGroupEventDto::class.java)
        val organizationEditChangeGroupEventDto = event as OrganizationEditChangeGroupEventDto
        assertThat(organizationEditChangeGroupEventDto.eventApplicable).isEqualTo(EventApplicability.SOURCE_MISMATCH)
    }


}