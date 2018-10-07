package de.helfenkannjeder.helfomat.core.organisation;

import de.helfenkannjeder.helfomat.core.event.DomainEvent;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationCreateEvent;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEditAddPictureEvent;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEditDeletePictureEvent;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEditNameEvent;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEvent;
import de.helfenkannjeder.helfomat.core.picture.PictureId;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Valentin Zickner
 */
public class OrganisationTest {

    @Test
    public void compareTo_withNull_returnsAllChanges() {
        // Arrange
        OrganisationId organisationId = new OrganisationId();
        Organisation organisation = new Organisation.Builder()
            .setId(organisationId)
            .setName("New Name")
            .setUrlName("new-name")
            .setOrganisationType(OrganisationType.ASB)
            .build();

        // Act
        List<OrganisationEvent> domainEvents = organisation.compareTo(null);

        // Assert
        assertThat(domainEvents)
            .isNotNull()
            .hasSize(1);
        DomainEvent domainEvent = domainEvents.get(0);
        assertThat(domainEvent)
            .isNotNull()
            .isInstanceOf(OrganisationCreateEvent.class);
        OrganisationCreateEvent organisationCreateEvent = (OrganisationCreateEvent) domainEvent;
        assertThat(organisationCreateEvent.getOrganisationId()).isEqualTo(organisationId);
        assertThat(organisationCreateEvent.getName()).isEqualTo("New Name");
        assertThat(organisationCreateEvent.getUrlName()).isEqualTo("new-name");
        assertThat(organisationCreateEvent.getOrganisationType()).isEqualTo(OrganisationType.ASB);
    }

    @Test
    public void compareTo_withWrongId_justNormalProcessingWithOriginalId() {
        // Arrange
        OrganisationId organisationId1 = new OrganisationId();
        OrganisationId organisationId2 = new OrganisationId();
        Organisation organisation1 = new Organisation.Builder().setId(organisationId1).setName("Original Name").build();
        Organisation organisation2 = new Organisation.Builder().setId(organisationId2).setName("New Name").build();

        // Act
        List<OrganisationEvent> domainEvents = organisation2.compareTo(organisation1);

        // Assert
        assertThat(domainEvents)
            .isNotNull()
            .hasSize(1);
        DomainEvent domainEvent = domainEvents.get(0);
        assertThat(domainEvent)
            .isNotNull()
            .isInstanceOf(OrganisationEditNameEvent.class);
        OrganisationEditNameEvent organisationEditNameEvent = (OrganisationEditNameEvent) domainEvent;
        assertThat(organisationEditNameEvent.getOrganisationId()).isEqualTo(organisationId1);
        assertThat(organisationEditNameEvent.getName()).isEqualTo("New Name");
    }

    @Test
    public void compareTo_withChangedName_returnsDifferenceObject() {
        // Arrange
        OrganisationId organisationId = new OrganisationId();
        Organisation organisation1 = new Organisation.Builder().setId(organisationId).setName("Original Name").build();
        Organisation organisation2 = new Organisation.Builder().setId(organisationId).setName("New Name").build();

        // Act
        List<OrganisationEvent> domainEvents = organisation2.compareTo(organisation1);

        // Assert
        assertThat(domainEvents)
            .isNotNull()
            .hasSize(1);
        DomainEvent domainEvent = domainEvents.get(0);
        assertThat(domainEvent)
            .isNotNull()
            .isInstanceOf(OrganisationEditNameEvent.class);
        OrganisationEditNameEvent organisationEditNameEvent = (OrganisationEditNameEvent) domainEvent;
        assertThat(organisationEditNameEvent.getOrganisationId()).isEqualTo(organisationId);
        assertThat(organisationEditNameEvent.getName()).isEqualTo("New Name");
    }

    @Test
    public void compareTo_withOneNewPicture_returnsChangedPictureEvents() {
        // Arrange
        OrganisationId organisationId = new OrganisationId();
        PictureId deletedPicture = new PictureId("879340a4-52ff-4c98-9dcc-b05e9f4a72ee");
        PictureId addedPicture = new PictureId("86078cc8-63a6-4246-a2d2-09dee7d9ced5");
        Organisation originalOrganisation = new Organisation.Builder()
            .setId(organisationId)
            .setPictures(Arrays.asList(
                new PictureId("f63f8a8b-edf5-47d6-b3bb-ccfd6b723b87"),
                deletedPicture,
                new PictureId("4c8090cc-7783-44d2-901c-e11335b79d09")
            ))
            .build();
        Organisation newOrganisation = new Organisation.Builder()
            .setId(organisationId)
            .setPictures(Arrays.asList(
                new PictureId("f63f8a8b-edf5-47d6-b3bb-ccfd6b723b87"),
                new PictureId("4c8090cc-7783-44d2-901c-e11335b79d09"),
                addedPicture
            ))
            .build();

        // Act
        List<OrganisationEvent> domainEvents = newOrganisation.compareTo(originalOrganisation);

        // Assert
        assertThat(domainEvents)
            .isNotNull()
            .hasSize(2);
        DomainEvent domainEvent1 = domainEvents.get(0);
        assertThat(domainEvent1)
            .isNotNull()
            .isInstanceOf(OrganisationEditDeletePictureEvent.class);
        OrganisationEditDeletePictureEvent organisationEditDeletePictureEvent = (OrganisationEditDeletePictureEvent) domainEvent1;
        assertThat(organisationEditDeletePictureEvent.getOrganisationId()).isEqualTo(organisationId);
        assertThat(organisationEditDeletePictureEvent.getPictureId()).isEqualTo(deletedPicture);
        DomainEvent domainEvent2 = domainEvents.get(1);
        assertThat(domainEvent2)
            .isNotNull()
            .isInstanceOf(OrganisationEditAddPictureEvent.class);
        OrganisationEditAddPictureEvent organisationEditAddPictureEvent = (OrganisationEditAddPictureEvent) domainEvent2;
        assertThat(organisationEditAddPictureEvent.getOrganisationId()).isEqualTo(organisationId);
        assertThat(organisationEditAddPictureEvent.getIndex()).isEqualTo(2);
        assertThat(organisationEditAddPictureEvent.getPictureId()).isEqualTo(addedPicture);
    }

    @Test
    public void compareTo_withObjectToEventsAndBackConvertion_verifyObjectsAreEquals() {
        // Arrange
        Organisation originalOrganisation = OrganisationTestDataFactory.ORGANISATION_1;

        // Act
        List<OrganisationEvent> domainEvents = originalOrganisation.compareTo(null);

        // Assert
        Organisation.Builder organisationBuilder = null;
        for (OrganisationEvent domainEvent : domainEvents) {
            organisationBuilder = domainEvent.applyOnOrganisationBuilder(organisationBuilder);
        }

        assertThat(organisationBuilder).isNotNull();
        Organisation organisation = organisationBuilder.build();
        assertThat(organisation.getId()).isEqualTo(originalOrganisation.getId());
        assertThat(organisation.getName()).isEqualTo(originalOrganisation.getName());
        assertThat(organisation.getUrlName()).isEqualTo(originalOrganisation.getUrlName());
        assertThat(organisation.getOrganisationType()).isEqualTo(originalOrganisation.getOrganisationType());
        assertThat(organisation.getDescription()).isEqualTo(originalOrganisation.getDescription());
        assertThat(organisation.getWebsite()).isEqualTo(originalOrganisation.getWebsite());
        assertThat(organisation.getLogo()).isEqualTo(originalOrganisation.getLogo());
        assertThat(organisation.getTeaserImage()).isEqualTo(originalOrganisation.getTeaserImage());
        assertThat(organisation.getDefaultAddress()).isEqualTo(originalOrganisation.getDefaultAddress());
        assertThat(organisation.getPictures()).isEqualTo(originalOrganisation.getPictures());
        assertThat(organisation.getContactPersons()).isEqualTo(originalOrganisation.getContactPersons());
        assertThat(organisation.getAddresses()).isEqualTo(originalOrganisation.getAddresses());
        assertThat(organisation.getQuestionAnswers()).isEqualTo(originalOrganisation.getQuestionAnswers());
        assertThat(organisation.getGroups()).isEqualTo(originalOrganisation.getGroups());
        assertThat(organisation.getAttendanceTimes()).isEqualTo(originalOrganisation.getAttendanceTimes());
        assertThat(organisation.getVolunteers()).isEqualTo(originalOrganisation.getVolunteers());
    }
}