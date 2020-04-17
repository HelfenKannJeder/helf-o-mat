package de.helfenkannjeder.helfomat.core.organization;

import de.helfenkannjeder.helfomat.core.event.DomainEvent;
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationCreateEvent;
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEditAddPictureEvent;
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEditDeletePictureEvent;
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEditNameEvent;
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEvent;
import de.helfenkannjeder.helfomat.core.picture.PictureId;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Valentin Zickner
 */
public class OrganizationTest {

    @Test
    public void compareTo_withNull_returnsAllChanges() {
        // Arrange
        OrganizationId organizationId = new OrganizationId();
        Organization organization = new Organization.Builder()
            .setId(organizationId)
            .setName("New Name")
            .setUrlName("new-name")
            .setOrganizationType(OrganizationType.ASB)
            .build();

        // Act
        List<OrganizationEvent> domainEvents = organization.compareTo(null);

        // Assert
        assertThat(domainEvents)
            .isNotNull()
            .hasSize(1);
        DomainEvent domainEvent = domainEvents.get(0);
        assertThat(domainEvent)
            .isNotNull()
            .isInstanceOf(OrganizationCreateEvent.class);
        OrganizationCreateEvent organizationCreateEvent = (OrganizationCreateEvent) domainEvent;
        assertThat(organizationCreateEvent.getOrganizationId()).isEqualTo(organizationId);
        assertThat(organizationCreateEvent.getName()).isEqualTo("New Name");
        assertThat(organizationCreateEvent.getUrlName()).isEqualTo("new-name");
        assertThat(organizationCreateEvent.getOrganizationType()).isEqualTo(OrganizationType.ASB);
    }

    @Test
    public void compareTo_withWrongId_justNormalProcessingWithOriginalId() {
        // Arrange
        OrganizationId organizationId1 = new OrganizationId();
        OrganizationId organizationId2 = new OrganizationId();
        Organization organization1 = new Organization.Builder().setId(organizationId1).setName("Original Name").build();
        Organization organization2 = new Organization.Builder().setId(organizationId2).setName("New Name").build();

        // Act
        List<OrganizationEvent> domainEvents = organization2.compareTo(organization1);

        // Assert
        assertThat(domainEvents)
            .isNotNull()
            .hasSize(1);
        DomainEvent domainEvent = domainEvents.get(0);
        assertThat(domainEvent)
            .isNotNull()
            .isInstanceOf(OrganizationEditNameEvent.class);
        OrganizationEditNameEvent organizationEditNameEvent = (OrganizationEditNameEvent) domainEvent;
        assertThat(organizationEditNameEvent.getOrganizationId()).isEqualTo(organizationId1);
        assertThat(organizationEditNameEvent.getName()).isEqualTo("New Name");
    }

    @Test
    public void compareTo_withChangedName_returnsDifferenceObject() {
        // Arrange
        OrganizationId organizationId = new OrganizationId();
        Organization organization1 = new Organization.Builder().setId(organizationId).setName("Original Name").build();
        Organization organization2 = new Organization.Builder().setId(organizationId).setName("New Name").build();

        // Act
        List<OrganizationEvent> domainEvents = organization2.compareTo(organization1);

        // Assert
        assertThat(domainEvents)
            .isNotNull()
            .hasSize(1);
        DomainEvent domainEvent = domainEvents.get(0);
        assertThat(domainEvent)
            .isNotNull()
            .isInstanceOf(OrganizationEditNameEvent.class);
        OrganizationEditNameEvent organizationEditNameEvent = (OrganizationEditNameEvent) domainEvent;
        assertThat(organizationEditNameEvent.getOrganizationId()).isEqualTo(organizationId);
        assertThat(organizationEditNameEvent.getName()).isEqualTo("New Name");
    }

    @Test
    public void compareTo_withOneNewPicture_returnsChangedPictureEvents() {
        // Arrange
        OrganizationId organizationId = new OrganizationId();
        PictureId deletedPicture = new PictureId("879340a4-52ff-4c98-9dcc-b05e9f4a72ee");
        PictureId addedPicture = new PictureId("86078cc8-63a6-4246-a2d2-09dee7d9ced5");
        Organization originalOrganization = new Organization.Builder()
            .setId(organizationId)
            .setPictures(Arrays.asList(
                new PictureId("f63f8a8b-edf5-47d6-b3bb-ccfd6b723b87"),
                deletedPicture,
                new PictureId("4c8090cc-7783-44d2-901c-e11335b79d09")
            ))
            .build();
        Organization newOrganization = new Organization.Builder()
            .setId(organizationId)
            .setPictures(Arrays.asList(
                new PictureId("f63f8a8b-edf5-47d6-b3bb-ccfd6b723b87"),
                new PictureId("4c8090cc-7783-44d2-901c-e11335b79d09"),
                addedPicture
            ))
            .build();

        // Act
        List<OrganizationEvent> domainEvents = newOrganization.compareTo(originalOrganization);

        // Assert
        assertThat(domainEvents)
            .isNotNull()
            .hasSize(2);
        DomainEvent domainEvent1 = domainEvents.get(0);
        assertThat(domainEvent1)
            .isNotNull()
            .isInstanceOf(OrganizationEditDeletePictureEvent.class);
        OrganizationEditDeletePictureEvent organizationEditDeletePictureEvent = (OrganizationEditDeletePictureEvent) domainEvent1;
        assertThat(organizationEditDeletePictureEvent.getOrganizationId()).isEqualTo(organizationId);
        assertThat(organizationEditDeletePictureEvent.getPictureId()).isEqualTo(deletedPicture);
        DomainEvent domainEvent2 = domainEvents.get(1);
        assertThat(domainEvent2)
            .isNotNull()
            .isInstanceOf(OrganizationEditAddPictureEvent.class);
        OrganizationEditAddPictureEvent organizationEditAddPictureEvent = (OrganizationEditAddPictureEvent) domainEvent2;
        assertThat(organizationEditAddPictureEvent.getOrganizationId()).isEqualTo(organizationId);
        assertThat(organizationEditAddPictureEvent.getIndex()).isEqualTo(2);
        assertThat(organizationEditAddPictureEvent.getPictureId()).isEqualTo(addedPicture);
    }

    @Test
    public void compareTo_withObjectToEventsAndBackConvertion_verifyObjectsAreEquals() {
        // Arrange
        Organization originalOrganization = OrganizationTestDataFactory.ORGANIZATION_1;

        // Act
        List<OrganizationEvent> domainEvents = originalOrganization.compareTo(null);

        // Assert
        Organization.Builder organizationBuilder = null;
        for (OrganizationEvent domainEvent : domainEvents) {
            organizationBuilder = domainEvent.applyOnOrganizationBuilder(organizationBuilder);
        }

        assertThat(organizationBuilder).isNotNull();
        Organization organization = organizationBuilder.build();
        assertThat(organization.getId()).isEqualTo(originalOrganization.getId());
        assertThat(organization.getName()).isEqualTo(originalOrganization.getName());
        assertThat(organization.getUrlName()).isEqualTo(originalOrganization.getUrlName());
        assertThat(organization.getOrganizationType()).isEqualTo(originalOrganization.getOrganizationType());
        assertThat(organization.getDescription()).isEqualTo(originalOrganization.getDescription());
        assertThat(organization.getWebsite()).isEqualTo(originalOrganization.getWebsite());
        assertThat(organization.getLogo()).isEqualTo(originalOrganization.getLogo());
        assertThat(organization.getTeaserImage()).isEqualTo(originalOrganization.getTeaserImage());
        assertThat(organization.getDefaultAddress()).isEqualTo(originalOrganization.getDefaultAddress());
        assertThat(organization.getPictures()).isEqualTo(originalOrganization.getPictures());
        assertThat(organization.getContactPersons()).isEqualTo(originalOrganization.getContactPersons());
        assertThat(organization.getAddresses()).isEqualTo(originalOrganization.getAddresses());
        assertThat(organization.getQuestionAnswers()).isEqualTo(originalOrganization.getQuestionAnswers());
        assertThat(organization.getGroups()).isEqualTo(originalOrganization.getGroups());
        assertThat(organization.getAttendanceTimes()).isEqualTo(originalOrganization.getAttendanceTimes());
        assertThat(organization.getVolunteers()).isEqualTo(originalOrganization.getVolunteers());
    }
}