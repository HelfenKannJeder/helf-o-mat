package de.helfenkannjeder.helfomat.infrastructure.batch.processor

import de.helfenkannjeder.helfomat.core.geopoint.GeoPoint
import de.helfenkannjeder.helfomat.core.organization.*
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEditNameEvent
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension

/**
 * @author Valentin Zickner
 */
@ExtendWith(MockitoExtension::class)
internal class OrganizationDifferenceProcessorTest {
    @Mock
    private lateinit var organizationRepository: OrganizationRepository

    @Mock
    private lateinit var generalOrganizationRepository: OrganizationRepository
    private lateinit var organizationDifferenceProcessor: OrganizationDifferenceProcessor

    @BeforeEach
    fun setUp() {
        organizationDifferenceProcessor = OrganizationDifferenceProcessor(organizationRepository, generalOrganizationRepository)
    }

    @Test
    fun process_withExistingOrganization_returnsNameChangedEvent() {
        // Arrange
        val organizationId = OrganizationId()
        val organization1 = Organization.Builder(
            id = organizationId,
            name = "New Organization Name",
            urlName = "organization-name",
            organizationType = OrganizationType.THW
        )
            .setDescription("Same Description")
            .build()
        val organization2 = Organization.Builder(
            id = organizationId,
            name = "Old Organization Name",
            urlName = "organization-name",
            organizationType = OrganizationType.THW
        )
            .setDescription("Same Description")
            .build()
        `when`(organizationRepository.findOrganizationWithSameTypeInDistance(organization1.defaultAddress, organization1.organizationType, 5L)).thenReturn(listOf(organization2))

        // Act
        val organizationDifferenceResult = organizationDifferenceProcessor.process(organization1)

        // Assert
        assertThat(organizationDifferenceResult).isNotNull
        assertThat(organizationDifferenceResult.first).isEqualTo(organization1)
        val organizationEvents = organizationDifferenceResult.second
        assertThat(organizationEvents).isNotNull
        assertThat(organizationEvents).hasSize(1)
        val actual = organizationEvents[0]
        assertThat(actual)
            .isNotNull
            .isInstanceOf(OrganizationEditNameEvent::class.java)
        val organizationEditNameEvent = actual as OrganizationEditNameEvent
        assertThat(organizationEditNameEvent.name).isEqualTo("New Organization Name")
    }

    @Test
    fun process_withOrganizationFromGeneralRepository_returnsNoEventsAndExistingUid() {
        // Arrange
        val resultOrganizationId = OrganizationId()
        val address = Address("Street 123", null, "Karlsruhe", "76131", GeoPoint(49.0, 12.0), "+49123456", "https://example.com")
        val organization1 = Organization.Builder(
            id = OrganizationId(),
            name = "New Organization Name",
            urlName = "organization-name",
            organizationType = OrganizationType.THW
        )
            .setDescription("Same Description")
            .setDefaultAddress(address)
            .build()
        val organization2 = Organization.Builder(
            id = resultOrganizationId,
            name = "Old Organization Name",
            urlName = "organization-name",
            organizationType = OrganizationType.THW
        )
            .setDescription("Same Description")
            .setDefaultAddress(address)
            .build()
        `when`(generalOrganizationRepository.findOrganizationWithSameTypeInDistance(organization1.defaultAddress, OrganizationType.THW, 5L)).thenReturn(listOf(organization2))

        // Act
        val organizationDifferenceResult = organizationDifferenceProcessor.process(organization1)

        // Assert
        assertThat(organizationDifferenceResult).isNotNull
        val organization = organizationDifferenceResult.first
        assertThat(organization).isNotNull
        assertThat(organization.id).isEqualTo(resultOrganizationId)
        assertThat(organization.name).isEqualTo("New Organization Name")
        assertThat(organizationDifferenceResult.second)
            .isNotNull
        assertThat(organizationDifferenceResult.second)
            .hasSize(0)
    }
}