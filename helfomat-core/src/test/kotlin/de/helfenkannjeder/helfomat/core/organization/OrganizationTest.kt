package de.helfenkannjeder.helfomat.core.organization

import de.helfenkannjeder.helfomat.core.event.DomainEvent
import de.helfenkannjeder.helfomat.core.organization.OrganizationTestDataFactory.ORGANIZATION_1_ABSTUETZEN
import de.helfenkannjeder.helfomat.core.organization.OrganizationTestDataFactory.ORGANIZATION_1_BAMBINIGRUPPE
import de.helfenkannjeder.helfomat.core.organization.OrganizationTestDataFactory.ORGANIZATION_1_BEL
import de.helfenkannjeder.helfomat.core.organization.OrganizationTestDataFactory.ORGANIZATION_1_BERGRUNGSGRUPPE
import de.helfenkannjeder.helfomat.core.organization.OrganizationTestDataFactory.ORGANIZATION_1_BERGRUNG_1
import de.helfenkannjeder.helfomat.core.organization.OrganizationTestDataFactory.ORGANIZATION_1_BERGRUNG_2
import de.helfenkannjeder.helfomat.core.organization.OrganizationTestDataFactory.ORGANIZATION_1_FELDKOCHHERD
import de.helfenkannjeder.helfomat.core.organization.OrganizationTestDataFactory.ORGANIZATION_1_JUGENDGRUPPE
import de.helfenkannjeder.helfomat.core.organization.OrganizationTestDataFactory.ORGANIZATION_1_N
import de.helfenkannjeder.helfomat.core.organization.OrganizationTestDataFactory.ORGANIZATION_1_OV_STAB
import de.helfenkannjeder.helfomat.core.organization.OrganizationTestDataFactory.ORGANIZATION_1_R
import de.helfenkannjeder.helfomat.core.organization.OrganizationTestDataFactory.ORGANIZATION_1_SCHWERE_BERGUNG
import de.helfenkannjeder.helfomat.core.organization.OrganizationTestDataFactory.ORGANIZATION_1_THV
import de.helfenkannjeder.helfomat.core.organization.OrganizationTestDataFactory.ORGANIZATION_1_ZUGTRUPP
import de.helfenkannjeder.helfomat.core.organization.event.*
import de.helfenkannjeder.helfomat.core.picture.PictureId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource

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
    fun compareTo_withObjectToEventsAndBackConversion_verifyObjectsAreEquals() {
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

    @ParameterizedTest
    @CsvSource(value = ["My Group,My Group", "My amazzing Group,My amazing Group"], delimiter = ',')
    fun compareTo_withDifferentGroupDescriptions_ensureGroupChangeEventIsGenerated(originalGroupName: String, newGroupName: String) {
        // Arrange
        val originalGroup = Group(originalGroupName, "Some description for my group")
        val original = originalGroup.toTestOrganization()
        val newGroup = Group(newGroupName, "New description for my group")
        val new = newGroup.toTestOrganization()

        // Act
        val difference = new.compareTo(original)

        // Assert
        assertThat(difference)
            .hasSize(1)
        val event = difference.get(0)
        assertThat(event).isInstanceOf(OrganizationEditChangeGroupEvent::class.java)
        val organizationEditGroupEvent = event as OrganizationEditChangeGroupEvent
        assertThat(organizationEditGroupEvent.indexOffset).isEqualTo(0)
        assertThat(organizationEditGroupEvent.oldGroup).isEqualTo(originalGroup)
        assertThat(organizationEditGroupEvent.group).isEqualTo(newGroup)

        assertThatOrganizationCanBeRebuildBasedOnEvents(original, difference, new)
    }

    @ParameterizedTest
    @CsvSource(value = ["My Group,My Group", "My amazzing Group,My amazing Group"], delimiter = ',')
    fun compareTo_withDifferentGroupDescriptionsButSameSimilarNameAndChangedOrder_ensureGroupIsPositionedCorrectly(originalGroupName: String, newGroupName: String) {
        // Arrange
        val notChangingGroup = Group("This is some test group", "with a strange test description")
        val originalGroup = Group(originalGroupName, "Some description for my group")
        val original = listOf(notChangingGroup, originalGroup).toTestOrganization()
        val newGroup = Group(newGroupName, "New description for my group")
        val new = listOf(newGroup, notChangingGroup).toTestOrganization()

        // Act
        val difference = new.compareTo(original)

        // Assert
        assertThat(difference)
            .hasSize(1)
        val event = difference.get(0)
        assertThat(event).isInstanceOf(OrganizationEditChangeGroupEvent::class.java)
        val organizationEditGroupEvent = event as OrganizationEditChangeGroupEvent
        assertThat(organizationEditGroupEvent.indexOffset).isEqualTo(-1)
        assertThat(organizationEditGroupEvent.oldGroup).isEqualTo(originalGroup)
        assertThat(organizationEditGroupEvent.group).isEqualTo(newGroup)

        assertThatOrganizationCanBeRebuildBasedOnEvents(original, difference, new)
    }

    @ParameterizedTest
    @CsvSource(value = ["My Group,A complete other Group", "My amazzing Group,It is just not amazing"], delimiter = ',')
    fun compareTo_withDifferentGroupNameAndDescriptionsOnDifferentPositions_ensureGroupChangeEventIsGenerated(originalGroupName: String, newGroupName: String) {
        // Arrange
        val notChangingGroup = Group("This is some test group", "with a strange test description")
        val originalGroup = Group(originalGroupName, "Some description for my group")
        val original = listOf(notChangingGroup, originalGroup).toTestOrganization()
        val newGroup = Group(newGroupName, "New description for my group")
        val new = listOf(newGroup, notChangingGroup).toTestOrganization()

        // Act
        val difference = new.compareTo(original)

        // Assert
        assertThat(difference)
            .hasSize(2)
        assertThatOrganizationCanBeRebuildBasedOnEvents(original, difference, new)
    }

    @Test
    fun compareTo_withChangingPosition_ensureGroupChangeEventIsGenerated() {
        // Arrange
        val notChangingGroup = Group("This is some test group", "with a strange test description")
        val originalGroup = Group("My group name", "Some description for my group")
        val original = listOf(notChangingGroup, originalGroup).toTestOrganization()
        val new = listOf(originalGroup, notChangingGroup).toTestOrganization()

        // Act
        val difference = new.compareTo(original)

        // Assert
        assertThat(difference).hasSize(1) // there is no need for an event to have the notChangingGroup, since moving one of both groups is enough
        val event = difference.get(0)
        assertThat(event).isInstanceOf(OrganizationEditChangeGroupEvent::class.java)
        val organizationEditGroupEvent = event as OrganizationEditChangeGroupEvent
        assertThat(organizationEditGroupEvent.indexOffset).isEqualTo(-1)
        assertThat(organizationEditGroupEvent.oldGroup).isEqualTo(originalGroup)
        assertThat(organizationEditGroupEvent.group).isEqualTo(originalGroup)

        assertThatOrganizationCanBeRebuildBasedOnEvents(original, difference, new)
    }

    @Test
    fun compareTo_withChangingPositionOfTwoGroups_ensureGroupChangeEventIsGenerated() {
        // Arrange
        val notChangingGroup1 = Group("This is some test group 1", "with a strange test description")
        val notChangingGroup2 = Group("This is some test group 2", "with a strange test description")
        val originalGroup1 = Group("My group name 1", "Some description for my group")
        val originalGroup2 = Group("My group name 2", "Some description for my group")
        val original = listOf(notChangingGroup1, notChangingGroup2, originalGroup1, originalGroup2).toTestOrganization()
        val new = listOf(originalGroup1, originalGroup2, notChangingGroup1, notChangingGroup2).toTestOrganization()

        // Act
        val difference = new.compareTo(original)

        // Assert
        assertThat(difference).hasSize(2)
        val event1 = difference[0]
        assertThat(event1).isInstanceOf(OrganizationEditChangeGroupEvent::class.java)
        val organizationEditGroupEvent1 = event1 as OrganizationEditChangeGroupEvent
        assertThat(organizationEditGroupEvent1.indexOffset).isEqualTo(-2)
        assertThat(organizationEditGroupEvent1.oldGroup).isEqualTo(originalGroup1)
        assertThat(organizationEditGroupEvent1.group).isEqualTo(originalGroup1)
        val event2 = difference[1]
        assertThat(event2).isInstanceOf(OrganizationEditChangeGroupEvent::class.java)
        val organizationEditGroupEvent2 = event2 as OrganizationEditChangeGroupEvent
        assertThat(organizationEditGroupEvent2.indexOffset).isEqualTo(-2)
        assertThat(organizationEditGroupEvent2.oldGroup).isEqualTo(originalGroup2)
        assertThat(organizationEditGroupEvent2.group).isEqualTo(originalGroup2)

        // ensure that we are able to rebuild it
        assertThatOrganizationCanBeRebuildBasedOnEvents(original, difference, new)
    }

    @ParameterizedTest
    @ValueSource(ints = [2, 3, 4, 5])
    fun compareTo_withMovingOneGroupDownInTheList_ensureGroupChangeEventIsOnlyGeneratedOnce(moveOffset: Int) {
        // Arrange
        val notChangingGroup1 = Group("This is some test group 1", "with a strange test description")
        val notChangingGroup2 = Group("This is some test group 2", "with a strange test description")
        val notChangingGroup3 = Group("This is some test group 3", "with a strange test description")
        val notChangingGroup4 = Group("This is some test group 4", "with a strange test description")
        val notChangingGroup5 = Group("This is some test group 5", "with a strange test description")
        val originalGroup = Group("My group name 1", "Some description for my group")
        val notChangingGroups = listOf(notChangingGroup1, notChangingGroup2, notChangingGroup3, notChangingGroup4, notChangingGroup5)
        val originalGroups = notChangingGroups.toMutableList()
        originalGroups.add(0, originalGroup)
        val original = originalGroups.toTestOrganization()
        val newGroups = notChangingGroups.toMutableList()
        newGroups.add(moveOffset, originalGroup)
        val new = newGroups.toTestOrganization()

        // Act
        val difference = new.compareTo(original)

        // Assert
        assertThat(difference).hasSize(1)
        val event1 = difference[0]
        assertThat(event1).isInstanceOf(OrganizationEditChangeGroupEvent::class.java)
        val organizationEditGroupEvent1 = event1 as OrganizationEditChangeGroupEvent
        assertThat(organizationEditGroupEvent1.indexOffset).isEqualTo(moveOffset)
        assertThat(organizationEditGroupEvent1.oldGroup).isEqualTo(originalGroup)
        assertThat(organizationEditGroupEvent1.group).isEqualTo(originalGroup)

        // ensure that we are able to rebuild it
        assertThatOrganizationCanBeRebuildBasedOnEvents(original, difference, new)
    }

    @ParameterizedTest
    @ValueSource(ints = [2, 3, 4, 5])
    fun compareTo_withMovingOneGroupDownInTheListAndChangingTheMovedElement_ensureGroupChangeEventIsOnlyGeneratedOnce(moveOffset: Int) {
        // Arrange
        val notChangingGroup1 = Group("This is some test group 1", "with a strange test description")
        val notChangingGroup2 = Group("This is some test group 2", "with a strange test description")
        val notChangingGroup3 = Group("This is some test group 3", "with a strange test description")
        val notChangingGroup4 = Group("This is some test group 4", "with a strange test description")
        val notChangingGroup5 = Group("This is some test group 5", "with a strange test description")
        val originalGroup = Group("My group name 1", "Some description for my group")
        val newGroup = Group("My group name 1", "Some different description for my group")
        val notChangingGroups = listOf(notChangingGroup1, notChangingGroup2, notChangingGroup3, notChangingGroup4, notChangingGroup5)
        val originalGroups = notChangingGroups.toMutableList()
        originalGroups.add(0, originalGroup)
        val original = originalGroups.toTestOrganization()
        val newGroups = notChangingGroups.toMutableList()
        newGroups.add(moveOffset, newGroup)
        val new = newGroups.toTestOrganization()

        // Act
        val difference = new.compareTo(original)

        // Assert
        assertThat(difference).hasSize(1)
        val event1 = difference[0]
        assertThat(event1).isInstanceOf(OrganizationEditChangeGroupEvent::class.java)
        val organizationEditGroupEvent1 = event1 as OrganizationEditChangeGroupEvent
        assertThat(organizationEditGroupEvent1.indexOffset).isEqualTo(moveOffset)
        assertThat(organizationEditGroupEvent1.oldGroup).isEqualTo(originalGroup)
        assertThat(organizationEditGroupEvent1.group).isEqualTo(newGroup)

        // ensure that we are able to rebuild it
        assertThatOrganizationCanBeRebuildBasedOnEvents(original, difference, new)
    }

    @ParameterizedTest
    @ValueSource(ints = [3, 4, 5])
    fun compareTo_withTwoGroupsMovingDownInTheList_ensureGroupChangeEventIsOnlyGeneratedTwice(moveOffset: Int) {
        // Arrange
        val notChangingGroup1 = Group("This is some test group 1", "with a strange test description")
        val notChangingGroup2 = Group("This is some test group 2", "with a strange test description")
        val notChangingGroup3 = Group("This is some test group 3", "with a strange test description")
        val notChangingGroup4 = Group("This is some test group 4", "with a strange test description")
        val notChangingGroup5 = Group("This is some test group 5", "with a strange test description")
        val originalGroup1 = Group("My group name 1", "Some description for my group")
        val originalGroup2 = Group("My group name 2", "Some description for my group")
        val notChangingGroups = listOf(notChangingGroup1, notChangingGroup2, notChangingGroup3, notChangingGroup4, notChangingGroup5)
        val originalGroups = notChangingGroups.toMutableList()
        originalGroups.add(0, originalGroup1)
        originalGroups.add(1, originalGroup2)
        val original = originalGroups.toTestOrganization()
        val newGroups = notChangingGroups.toMutableList()
        newGroups.add(moveOffset, originalGroup1)
        newGroups.add(moveOffset + 1, originalGroup2)
        val new = newGroups.toTestOrganization()

        // Act
        val difference = new.compareTo(original)

        // Assert
        assertThat(difference).hasSize(2)
        val event1 = difference.get(0)
        assertThat(event1).isInstanceOf(OrganizationEditChangeGroupEvent::class.java)
        val organizationEditGroupEvent1 = event1 as OrganizationEditChangeGroupEvent
        // o1, o2, n1, n2, n3, n4, n5
        // n1, n2, n3, o1, o2, n4, n5 -> result with move three
        // since we move first o1, and it must result after n3 nwe need to have index offset 4
        val numberOfElementsMoved = 2
        assertThat(organizationEditGroupEvent1.indexOffset).isEqualTo(moveOffset + (numberOfElementsMoved - 1))
        assertThat(organizationEditGroupEvent1.oldGroup).isEqualTo(originalGroup1)
        assertThat(organizationEditGroupEvent1.group).isEqualTo(originalGroup1)
        val event2 = difference.get(1)
        assertThat(event2).isInstanceOf(OrganizationEditChangeGroupEvent::class.java)
        val organizationEditGroupEvent2 = event2 as OrganizationEditChangeGroupEvent
        assertThat(organizationEditGroupEvent2.indexOffset).isEqualTo(moveOffset + (numberOfElementsMoved - 1))
        assertThat(organizationEditGroupEvent2.oldGroup).isEqualTo(originalGroup2)
        assertThat(organizationEditGroupEvent2.group).isEqualTo(originalGroup2)

        // ensure that we are able to rebuild it
        assertThatOrganizationCanBeRebuildBasedOnEvents(original, difference, new)
    }

    @ParameterizedTest
    @ValueSource(ints = [0, 1, 2, 3])
    fun compareTo_withInsertingAtPosition_ensureThatPositionIsCorrect(index: Int) {
        val notChangingGroup1 = Group("This is some test group 1", "with a strange test description")
        val notChangingGroup2 = Group("This is some test group 2", "with a strange test description")
        val notChangingGroup3 = Group("This is some test group 3", "with a strange test description")
        val newGroup = Group("My group name", "Some description for my group")
        val original = listOf(notChangingGroup1, notChangingGroup2, notChangingGroup3).toTestOrganization()
        val newList = mutableListOf(notChangingGroup1, notChangingGroup2, notChangingGroup3)
        newList.add(index, newGroup)
        val new = newList.toTestOrganization()

        // Act
        val difference = new.compareTo(original)

        // Assert
        assertThat(difference).hasSize(1)
        val event = difference.get(0)
        assertThat(event).isInstanceOf(OrganizationEditAddGroupEvent::class.java)
        val organizationEditGroupEvent = event as OrganizationEditAddGroupEvent
        assertThat(organizationEditGroupEvent.index).isEqualTo(index)
        assertThat(organizationEditGroupEvent.group).isEqualTo(newGroup)

        assertThatOrganizationCanBeRebuildBasedOnEvents(original, difference, new)
    }

    @ParameterizedTest
    @ValueSource(ints = [0, 1, 2, 3])
    fun compareTo_withInsertingMultipleElementsAtPosition_ensureThatResultIsBuildToTheSame(index: Int) {
        val notChangingGroup1 = Group("This is some test group 1", "with a strange test description")
        val notChangingGroup2 = Group("This is some test group 2", "with a strange test description")
        val notChangingGroup3 = Group("This is some test group 3", "with a strange test description")
        val newGroup1 = Group("My group name 1", "Some description for my group")
        val newGroup2 = Group("My group name 2", "Some description for my group")
        val original = listOf(notChangingGroup1, notChangingGroup2, notChangingGroup3).toTestOrganization()
        val newList = mutableListOf(newGroup1, notChangingGroup1, notChangingGroup2, notChangingGroup3)
        newList.add(index, newGroup2)
        val new = newList.toTestOrganization()

        // Act
        val organizationEvents = new.compareTo(original)

        // Assert
        assertThatOrganizationCanBeRebuildBasedOnEvents(original, organizationEvents, new)
    }

    @Test
    fun compareTo_withComplexGroupStructure_ensureGroupOrderIsCorrectAfterRebuild() {
        // Arrange
        val originalOrganization = listOf(
            ORGANIZATION_1_OV_STAB,
            ORGANIZATION_1_JUGENDGRUPPE,
            ORGANIZATION_1_BAMBINIGRUPPE,
            ORGANIZATION_1_ZUGTRUPP,
            ORGANIZATION_1_BERGRUNG_1,
            ORGANIZATION_1_BERGRUNG_2,
            ORGANIZATION_1_R,
            ORGANIZATION_1_BEL,
            ORGANIZATION_1_ABSTUETZEN,
            ORGANIZATION_1_FELDKOCHHERD,
            ORGANIZATION_1_THV
        ).toTestOrganization()

        val newOrganization = listOf(
            ORGANIZATION_1_OV_STAB,
            ORGANIZATION_1_ZUGTRUPP,
            ORGANIZATION_1_BERGRUNGSGRUPPE,
            ORGANIZATION_1_SCHWERE_BERGUNG,
            ORGANIZATION_1_R,
            ORGANIZATION_1_N,
            ORGANIZATION_1_JUGENDGRUPPE,
            ORGANIZATION_1_BAMBINIGRUPPE,
            ORGANIZATION_1_ABSTUETZEN,
            ORGANIZATION_1_FELDKOCHHERD,
            ORGANIZATION_1_THV
        ).toTestOrganization()

        // Act
        val organizationEvents = newOrganization.compareTo(originalOrganization)

        // Assert
        assertThatOrganizationCanBeRebuildBasedOnEvents(originalOrganization, organizationEvents, newOrganization)
    }

    @Test
    fun compareTo_withDuplicateGroupRemovingOne_ensureOneIsRemoved() {
        // Arrange
        val originalOrganization = listOf(
            ORGANIZATION_1_OV_STAB,
            ORGANIZATION_1_OV_STAB
        ).toTestOrganization()

        val newOrganization = listOf(
            ORGANIZATION_1_OV_STAB
        ).toTestOrganization()

        // Act
        val organizationEvents = newOrganization.compareTo(originalOrganization)

        // Assert
        assertThat(organizationEvents).hasSize(1)
        val organizationEvent = organizationEvents[0]
        assertThat(organizationEvent).isInstanceOf(OrganizationEditDeleteGroupEvent::class.java)
        assertThat(organizationEvent.organizationId).isEqualTo(originalOrganization.id)
        val organizationEditDeleteGroupEvent = organizationEvent as OrganizationEditDeleteGroupEvent
        assertThat(organizationEditDeleteGroupEvent.group).isEqualTo(ORGANIZATION_1_OV_STAB)

        assertThatOrganizationCanBeRebuildBasedOnEvents(originalOrganization, organizationEvents, newOrganization)
    }

    private fun assertThatOrganizationCanBeRebuildBasedOnEvents(original: Organization, difference: List<OrganizationEvent>, new: Organization) {
        var organizationBuilder: Organization.Builder? = Organization.Builder(original)
        for (domainEvent in difference) {
            organizationBuilder = domainEvent.applyOnOrganizationBuilder(organizationBuilder)
        }
        assertThat(organizationBuilder).isNotNull
        val organization = organizationBuilder?.build() ?: throw IllegalStateException()
        assertThat(organization.groups).containsExactlyElementsOf(new.groups)
    }

}

fun Group.toTestOrganization() = listOf(this).toTestOrganization()
fun List<Group>.toTestOrganization() = Organization.Builder(OrganizationTestDataFactory.ORGANIZATION_1)
    .setGroups(this)
    .build()