package de.helfenkannjeder.helfomat.core.organization

import de.helfenkannjeder.helfomat.core.event.DomainEvent
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationCreateEvent
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEditAddPictureEvent
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEditDeletePictureEvent
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEditNameEvent
import de.helfenkannjeder.helfomat.core.picture.PictureId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

/**
 * @author Valentin Zickner
 */
class OrganizationTest {
    @Test
    fun compareTo_withNull_returnsAllChanges() {
        // Arrange
        val organizationId = OrganizationId()
        val organization = Organization(
            id = organizationId,
            name = "New Name",
            urlName = "new-name",
            organizationType = OrganizationType.ASB
        )

        // Act
        val domainEvents = organization.compareTo(null)

        // Assert
        assertThat(domainEvents)
            .hasSize(1)
        val domainEvent: DomainEvent = domainEvents[0]
        assertThat(domainEvent)
            .isInstanceOf(OrganizationCreateEvent::class.java)
        val (organizationId1, name, urlName, organizationType) = domainEvent as OrganizationCreateEvent
        assertThat(organizationId1).isEqualTo(organizationId)
        assertThat(name).isEqualTo("New Name")
        assertThat(urlName).isEqualTo("new-name")
        assertThat(organizationType).isEqualTo(OrganizationType.ASB)
    }

    @Test
    fun compareTo_withWrongId_justNormalProcessingWithOriginalId() {
        // Arrange
        val organizationId1 = OrganizationId()
        val organizationId2 = OrganizationId()
        val organization1 = Organization(id = organizationId1, name = "Original Name", urlName = "url-name", organizationType = OrganizationType.ASB)
        val organization2 = Organization(id = organizationId2, name = "New Name", urlName = "url-name", organizationType = OrganizationType.ASB)

        // Act
        val domainEvents = organization2.compareTo(organization1)

        // Assert
        assertThat(domainEvents)
            .isNotNull
            .hasSize(1)
        val domainEvent: DomainEvent = domainEvents[0]
        assertThat(domainEvent)
            .isNotNull
            .isInstanceOf(OrganizationEditNameEvent::class.java)
        val (organizationId, name) = domainEvent as OrganizationEditNameEvent
        assertThat(organizationId).isEqualTo(organizationId1)
        assertThat(name).isEqualTo("New Name")
    }

    @Test
    fun compareTo_withChangedName_returnsDifferenceObject() {
        // Arrange
        val organizationId = OrganizationId()
        val organization1 = Organization(id = organizationId, name = "Original Name", urlName = "url-name", organizationType = OrganizationType.ASB)
        val organization2 = Organization(id = organizationId, name = "New Name", urlName = "url-name", organizationType = OrganizationType.ASB)

        // Act
        val domainEvents = organization2.compareTo(organization1)

        // Assert
        assertThat(domainEvents)
            .isNotNull
            .hasSize(1)
        val domainEvent: DomainEvent = domainEvents[0]
        assertThat(domainEvent)
            .isNotNull
            .isInstanceOf(OrganizationEditNameEvent::class.java)
        val (organizationId1, name) = domainEvent as OrganizationEditNameEvent
        assertThat(organizationId1).isEqualTo(organizationId)
        assertThat(name).isEqualTo("New Name")
    }

    @Test
    fun compareTo_withOneNewPicture_returnsChangedPictureEvents() {
        // Arrange
        val organizationId = OrganizationId()
        val deletedPicture = PictureId("879340a4-52ff-4c98-9dcc-b05e9f4a72ee")
        val addedPicture = PictureId("86078cc8-63a6-4246-a2d2-09dee7d9ced5")
        val originalOrganization = Organization(
            id = organizationId,
            name = "My Organization",
            urlName = "my-organization",
            organizationType = OrganizationType.ASB,
            pictures = listOf(
                PictureId("f63f8a8b-edf5-47d6-b3bb-ccfd6b723b87"),
                deletedPicture,
                PictureId("4c8090cc-7783-44d2-901c-e11335b79d09")
            )
        )
        val newOrganization = Organization(
            id = organizationId,
            name = "My Organization",
            urlName = "my-organization",
            organizationType = OrganizationType.ASB,
            pictures = listOf(
                PictureId("f63f8a8b-edf5-47d6-b3bb-ccfd6b723b87"),
                PictureId("4c8090cc-7783-44d2-901c-e11335b79d09"),
                addedPicture
            )
        )

        // Act
        val domainEvents = newOrganization.compareTo(originalOrganization)

        // Assert
        assertThat(domainEvents)
            .isNotNull
            .hasSize(2)
        val domainEvent1: DomainEvent = domainEvents[0]
        assertThat(domainEvent1)
            .isNotNull
            .isInstanceOf(OrganizationEditDeletePictureEvent::class.java)
        val (organizationId1, pictureId) = domainEvent1 as OrganizationEditDeletePictureEvent
        assertThat(organizationId1).isEqualTo(organizationId)
        assertThat(pictureId).isEqualTo(deletedPicture)
        val domainEvent2: DomainEvent = domainEvents[1]
        assertThat(domainEvent2)
            .isNotNull
            .isInstanceOf(OrganizationEditAddPictureEvent::class.java)
        val (organizationId2, index, pictureId1) = domainEvent2 as OrganizationEditAddPictureEvent
        assertThat(organizationId2).isEqualTo(organizationId)
        assertThat(index).isEqualTo(2)
        assertThat(pictureId1).isEqualTo(addedPicture)
    }

    @Test
    fun compareTo_withObjectToEventsAndBackConvertion_verifyObjectsAreEquals() {
        // Arrange
        val originalOrganization = OrganizationTestDataFactory.ORGANIZATION_1

        // Act
        val domainEvents = originalOrganization.compareTo(null)

        // Assert
        var organizationBuilder: Organization.Builder? = Organization.Builder(
            id = OrganizationId(),
            name = "Test",
            urlName = "test",
            organizationType = OrganizationType.DRV
        )
        for (domainEvent in domainEvents) {
            organizationBuilder = domainEvent.applyOnOrganizationBuilder(organizationBuilder)
        }
        assertThat(organizationBuilder).isNotNull
        val (id, name, urlName, organizationType, description, website, logo, teaserImage, defaultAddress, pictures, contactPersons, addresses, questionAnswers, _, groups, attendanceTimes, volunteers) = organizationBuilder!!.build()
        assertThat(id).isEqualTo(originalOrganization.id)
        assertThat(name).isEqualTo(originalOrganization.name)
        assertThat(urlName).isEqualTo(originalOrganization.urlName)
        assertThat(organizationType).isEqualTo(originalOrganization.organizationType)
        assertThat(description).isEqualTo(originalOrganization.description)
        assertThat(website).isEqualTo(originalOrganization.website)
        assertThat(logo).isEqualTo(originalOrganization.logo)
        assertThat(teaserImage).isEqualTo(originalOrganization.teaserImage)
        assertThat(defaultAddress).isEqualTo(originalOrganization.defaultAddress)
        assertThat(pictures).isEqualTo(originalOrganization.pictures)
        assertThat(contactPersons).isEqualTo(originalOrganization.contactPersons)
        assertThat(addresses).isEqualTo(originalOrganization.addresses)
        assertThat(questionAnswers).isEqualTo(originalOrganization.questionAnswers)
        assertThat(groups).isEqualTo(originalOrganization.groups)
        assertThat(attendanceTimes).isEqualTo(originalOrganization.attendanceTimes)
        assertThat(volunteers).isEqualTo(originalOrganization.volunteers)
    }
}