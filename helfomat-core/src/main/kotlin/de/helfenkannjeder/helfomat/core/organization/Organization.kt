package de.helfenkannjeder.helfomat.core.organization

import de.helfenkannjeder.helfomat.core.organization.event.*
import de.helfenkannjeder.helfomat.core.picture.PictureId
import org.simmetrics.StringMetric
import org.simmetrics.builders.StringMetricBuilder
import org.simmetrics.metrics.CosineSimilarity
import org.simmetrics.simplifiers.Simplifiers
import org.simmetrics.tokenizers.Tokenizers
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min


/**
 * @author Valentin Zickner
 */
data class Organization(
    var id: OrganizationId,
    var name: String,
    var urlName: String,
    var organizationType: OrganizationType,
    var description: String? = null,
    var website: String? = null,
    var logo: PictureId? = null,
    var teaserImage: PictureId? = null,
    var defaultAddress: Address? = null,
    var pictures: List<PictureId> = emptyList(),
    var contactPersons: List<ContactPerson> = emptyList(),
    var addresses: List<Address> = emptyList(),
    var questionAnswers: List<QuestionAnswer> = emptyList(),
    var mapPin: String? = null,
    var groups: List<Group> = emptyList(),
    var attendanceTimes: List<AttendanceTime> = emptyList(),
    var volunteers: List<Volunteer> = emptyList()
) {

    fun compareTo(o: Organization?): List<OrganizationEvent> {
        var organization = o
        val differences: MutableList<OrganizationEvent> = ArrayList()
        if (organization == null) {
            differences.add(OrganizationCreateEvent(
                id,
                name,
                urlName,
                organizationType
            ))
            organization = Organization(
                id = id,
                name = name,
                urlName = urlName,
                organizationType = organizationType
            )
        }
        val id = organization.id
        if (organization.name != name) {
            differences.add(OrganizationEditNameEvent(id, name))
        }
        if (organization.urlName != urlName) {
            differences.add(OrganizationEditUrlNameEvent(id, urlName))
        }
        if (organization.description != description) {
            differences.add(OrganizationEditDescriptionEvent(id, description))
        }
        if (organization.website != website) {
            differences.add(OrganizationEditWebsiteEvent(id, website))
        }
        if (organization.logo != logo) {
            differences.add(OrganizationEditLogoEvent(id, logo))
        }
        if (organization.teaserImage != teaserImage) {
            differences.add(OrganizationEditTeaserImageEvent(id, teaserImage))
        }
        if (organization.defaultAddress != defaultAddress) {
            differences.add(OrganizationEditDefaultAddressEvent(id, defaultAddress))
        }
        differences.addAll(compare(
            organization.pictures,
            pictures,
            object : ElementComparisonStrategy<PictureId, OrganizationEditChangePictureEvent> {
                override fun create(index: Int, element: PictureId) = OrganizationEditAddPictureEvent(id, index, element)
                override fun update(indexOffset: Int, oldElement: PictureId, element: PictureId) = OrganizationEditChangePictureEvent(id, indexOffset, element)
                override fun delete(element: PictureId) = OrganizationEditDeletePictureEvent(id, element)
                override fun isUpdateContent(event: OrganizationEditChangePictureEvent) = false
                override fun updateWithChangedIndex(indexOffset: Int, event: OrganizationEditChangePictureEvent) = OrganizationEditChangePictureEvent(id, indexOffset, event.pictureId)
                override fun compareWithMetric(element1: PictureId, element2: PictureId) = 0.0f
            }
        ))
        differences.addAll(compare(
            organization.contactPersons,
            contactPersons,
            object : ElementComparisonStrategy<ContactPerson, OrganizationEditChangeContactPersonEvent> {
                override fun create(index: Int, element: ContactPerson): OrganizationEvent = OrganizationEditAddContactPersonEvent(id, index, element)
                override fun update(indexOffset: Int, oldElement: ContactPerson, element: ContactPerson): OrganizationEditChangeContactPersonEvent = OrganizationEditChangeContactPersonEvent(id, indexOffset, oldElement, element)
                override fun delete(element: ContactPerson): OrganizationEvent = OrganizationEditDeleteContactPersonEvent(id, element)
                override fun isUpdateContent(event: OrganizationEditChangeContactPersonEvent): Boolean = event.contactPerson != event.oldContactPerson
                override fun updateWithChangedIndex(indexOffset: Int, event: OrganizationEditChangeContactPersonEvent): OrganizationEditChangeContactPersonEvent = OrganizationEditChangeContactPersonEvent(id, indexOffset, event.oldContactPerson, event.contactPerson)
                override fun isSimilar(element1: ContactPerson, element2: ContactPerson): Boolean = element1.firstname == element2.firstname && element2.lastname == element2.lastname
                override fun compareWithMetric(element1: ContactPerson, element2: ContactPerson) = metric.compare("${element1.firstname} ${element1.lastname}", "${element2.firstname} ${element2.lastname}")
            }
        ))
        differences.addAll(compare(
            organization.addresses,
            addresses,
            object : ElementComparisonStrategy<Address, OrganizationEditChangeAddressEvent> {
                override fun create(index: Int, element: Address) = OrganizationEditAddAddressEvent(id, index, element)
                override fun update(indexOffset: Int, oldElement: Address, element: Address) = OrganizationEditChangeAddressEvent(id, indexOffset, oldElement, element)
                override fun delete(element: Address) = OrganizationEditDeleteAddressEvent(id, element)
                override fun isUpdateContent(event: OrganizationEditChangeAddressEvent) = event.address != event.oldAddress
                override fun updateWithChangedIndex(indexOffset: Int, event: OrganizationEditChangeAddressEvent) = OrganizationEditChangeAddressEvent(id, indexOffset, event.oldAddress, event.address)
                override fun isSimilar(element1: Address, element2: Address) = element1.addressAppendix == element2.addressAppendix
                override fun compareWithMetric(element1: Address, element2: Address) = max((5.0 - element1.location.distanceInKm(element2.location)) / 5.0, 0.0).toFloat()
            }
        ))
        differences.addAll(compare(
            organization.questionAnswers,
            questionAnswers,
            object : ElementComparisonStrategy<QuestionAnswer, OrganizationEditChangeQuestionAnswerEvent> {
                override fun create(index: Int, element: QuestionAnswer): OrganizationEvent = OrganizationEditAddQuestionAnswerEvent(id, index, element)
                override fun update(indexOffset: Int, oldElement: QuestionAnswer, element: QuestionAnswer): OrganizationEditChangeQuestionAnswerEvent = OrganizationEditChangeQuestionAnswerEvent(id, indexOffset, oldElement, element)
                override fun delete(element: QuestionAnswer): OrganizationEvent = OrganizationEditDeleteQuestionAnswerEvent(id, element)
                override fun isUpdateContent(event: OrganizationEditChangeQuestionAnswerEvent): Boolean = event.oldQuestionAnswer != event.questionAnswer
                override fun isSimilar(element1: QuestionAnswer, element2: QuestionAnswer): Boolean = element1.questionId == element2.questionId
                override fun updateWithChangedIndex(indexOffset: Int, event: OrganizationEditChangeQuestionAnswerEvent): OrganizationEditChangeQuestionAnswerEvent = OrganizationEditChangeQuestionAnswerEvent(id, indexOffset, event.oldQuestionAnswer, event.questionAnswer)
            }
        ))
        differences.addAll(compare(
            organization.groups,
            groups,
            object : ElementComparisonStrategy<Group, OrganizationEditChangeGroupEvent> {
                override fun create(index: Int, element: Group) = OrganizationEditAddGroupEvent(id, index, element)
                override fun update(indexOffset: Int, oldElement: Group, element: Group) = OrganizationEditChangeGroupEvent(id, indexOffset, oldElement, element)
                override fun delete(element: Group) = OrganizationEditDeleteGroupEvent(id, element)
                override fun isUpdateContent(event: OrganizationEditChangeGroupEvent) = event.oldGroup != event.group
                override fun updateWithChangedIndex(indexOffset: Int, event: OrganizationEditChangeGroupEvent) = OrganizationEditChangeGroupEvent(id, indexOffset, event.oldGroup, event.group)
                override fun isSimilar(element1: Group, element2: Group) = element1.name == element2.name
                override fun compareWithMetric(element1: Group, element2: Group) = metric.compare(element1.name, element2.name)
            }
        ))
        differences.addAll(compare(
            organization.attendanceTimes,
            attendanceTimes,
            object : ElementComparisonStrategy<AttendanceTime, OrganizationEditChangeAttendanceTimeEvent> {
                override fun create(index: Int, element: AttendanceTime) = OrganizationEditAddAttendanceTimeEvent(id, index, element)
                override fun update(indexOffset: Int, oldElement: AttendanceTime, element: AttendanceTime) = OrganizationEditChangeAttendanceTimeEvent(id, indexOffset, oldElement, element)
                override fun delete(element: AttendanceTime) = OrganizationEditDeleteAttendanceTimeEvent(id, element)
                override fun isUpdateContent(event: OrganizationEditChangeAttendanceTimeEvent) = event.attendanceTime != event.oldAttendanceTime
                override fun updateWithChangedIndex(indexOffset: Int, event: OrganizationEditChangeAttendanceTimeEvent) = OrganizationEditChangeAttendanceTimeEvent(id, indexOffset, event.oldAttendanceTime, event.attendanceTime)
                override fun compareWithMetric(element1: AttendanceTime, element2: AttendanceTime): Float = if (element1.day == element2.day) 1.0f else 0.0f
            }
        ))
        differences.addAll(getDiff(organization.volunteers, volunteers).map { OrganizationEditDeleteVolunteerEvent(id, it.first) })
        differences.addAll(getDiff(volunteers, organization.volunteers).map { OrganizationEditAddVolunteerEvent(id, it.second, it.first) })
        return differences
    }

    data class Builder(
        var id: OrganizationId,
        var name: String,
        var urlName: String,
        var organizationType: OrganizationType,
        var description: String? = null,
        var website: String? = null,
        var logo: PictureId? = null,
        var teaserImage: PictureId? = null,
        var defaultAddress: Address? = null,
        var pictures: MutableList<PictureId> = ArrayList(),
        var contactPersons: MutableList<ContactPerson> = ArrayList(),
        var addresses: MutableList<Address> = ArrayList(),
        var questionAnswers: MutableList<QuestionAnswer> = ArrayList(),
        var mapPin: String? = null,
        var groups: MutableList<Group> = ArrayList(),
        var attendanceTimes: MutableList<AttendanceTime> = ArrayList(),
        var volunteers: MutableList<Volunteer> = ArrayList()
    ) {

        constructor(organization: Organization) : this(organization.id, organization.name, organization.urlName, organization.organizationType) {
            this.description = organization.description
            this.website = organization.website
            this.logo = organization.logo
            this.teaserImage = organization.teaserImage
            this.defaultAddress = organization.defaultAddress
            this.pictures = organization.pictures.toMutableList()
            this.contactPersons = organization.contactPersons.toMutableList()
            this.addresses = organization.addresses.toMutableList()
            this.questionAnswers = organization.questionAnswers.toMutableList()
            this.mapPin = organization.mapPin
            this.groups = organization.groups.toMutableList()
            this.attendanceTimes = organization.attendanceTimes.toMutableList()
            this.volunteers = organization.volunteers.toMutableList()
        }

        fun setId(id: OrganizationId) = apply { this.id = id }
        fun setName(name: String) = apply { this.name = name }
        fun setUrlName(urlName: String) = apply { this.urlName = urlName }
        fun setOrganizationType(organizationType: OrganizationType) = apply { this.organizationType = organizationType }
        fun setDescription(description: String?) = apply { this.description = description }
        fun setWebsite(website: String?) = apply { this.website = website }
        fun setLogo(logo: PictureId?) = apply { this.logo = logo }
        fun setTeaserImage(teaserImage: PictureId?) = apply { this.teaserImage = teaserImage }
        fun setDefaultAddress(defaultAddress: Address?) = apply { this.defaultAddress = defaultAddress }
        fun setPictures(pictures: List<PictureId>) = apply { this.pictures = pictures.toMutableList() }
        fun setContactPersons(contactPersons: List<ContactPerson>) = apply { this.contactPersons = contactPersons.toMutableList() }
        fun setAddresses(addresses: List<Address>) = apply { this.addresses = addresses.toMutableList() }
        fun setQuestionAnswers(questionAnswers: List<QuestionAnswer>) = apply { this.questionAnswers = questionAnswers.toMutableList() }
        fun setMapPin(mapPin: String?) = apply { this.mapPin = mapPin }
        fun setGroups(groups: List<Group>) = apply { this.groups = groups.toMutableList() }
        fun setAttendanceTimes(attendanceTimes: List<AttendanceTime>) = apply { this.attendanceTimes = attendanceTimes.toMutableList() }
        fun setVolunteers(volunteers: List<Volunteer>) = apply { this.volunteers = volunteers.toMutableList() }

        fun build(): Organization = Organization(
            id = id,
            name = name,
            urlName = urlName,
            organizationType = organizationType,
            description = description,
            website = website,
            logo = logo,
            teaserImage = teaserImage,
            defaultAddress = defaultAddress,
            pictures = pictures,
            contactPersons = contactPersons,
            addresses = addresses,
            questionAnswers = questionAnswers,
            mapPin = mapPin,
            groups = groups,
            attendanceTimes = attendanceTimes,
            volunteers = volunteers
        )
    }

    private fun <T, E : OrganizationEvent> compare(originalOrganizationElements: List<T>, newOrganizationElements: List<T>, elementComparisonStrategy: ElementComparisonStrategy<T, E>): List<OrganizationEvent> {
        val delete = getDiff(originalOrganizationElements, newOrganizationElements).map { it.first }
        val add = getDiff(newOrganizationElements, originalOrganizationElements).map { it.first }
        val resultDelete = delete.toMutableList()
        val resultAdd = add.toMutableList()
        val conversion = hashMapOf<T, T>()
        for (deleteElement in delete) {
            val addSearchResult = resultAdd.filter { elementComparisonStrategy.isSimilar(deleteElement, it) }
            if (addSearchResult.isNotEmpty()) {
                val addElement = addSearchResult[0]
                conversion[addElement] = deleteElement
                resultDelete.remove(deleteElement)
                resultAdd.remove(addElement)
            }
            val byDifference = resultAdd.sortedBy { 1.0 - elementComparisonStrategy.compareWithMetric(deleteElement, it) }
            if (byDifference.isNotEmpty() && elementComparisonStrategy.compareWithMetric(deleteElement, byDifference.first()) >= 0.5) {
                val addElement = byDifference[0]
                conversion[addElement] = deleteElement
                resultDelete.remove(deleteElement)
                resultAdd.remove(addElement)
            }
        }

        val originalElements = originalOrganizationElements.toMutableList()
        val newElements = newOrganizationElements.toMutableList()
        resultAdd.forEach { newElements.remove(it) }
        resultDelete.forEach { originalElements.remove(it) }

        val endOfIteration = min(originalElements.size, newElements.size)
        var resultEditEvents: MutableList<E> = mutableListOf()

        var editEventsInARow = 0
        var currentIndexOffset: Int? = null
        var furtherElementsToMove = 0
        var furtherElementsToMoveOffset: Int? = null
        for (index in 0 until endOfIteration) {
            val element = newElements[index]
            val oldElement = conversion[element] ?: element

            val oldIndex = originalElements.indexOf(oldElement)
            if (index != oldIndex || element != oldElement || furtherElementsToMove > 0) {
                var indexOffset: Int
                if (furtherElementsToMove > 0 && furtherElementsToMoveOffset != null) {
                    indexOffset = furtherElementsToMoveOffset
                    furtherElementsToMove--
                } else {
                    indexOffset = index - oldIndex
                }
                if (indexOffset != 0) {
                    if (indexOffset == currentIndexOffset) {
                        editEventsInARow++
                    } else {
                        currentIndexOffset = indexOffset
                        editEventsInARow = 1
                    }
                } else {
                    if (currentIndexOffset != null && abs(currentIndexOffset) < editEventsInARow) {
                        resultEditEvents = removeLastEventsInCaseOnlyPositionChanged(resultEditEvents, editEventsInARow, elementComparisonStrategy).toMutableList()
                        val elementsToMove = abs(currentIndexOffset)
                        furtherElementsToMove = elementsToMove - 1 // we already move the current one
                        furtherElementsToMoveOffset = editEventsInARow + elementsToMove - 1 // we need to skip over further elements we need to move
                        indexOffset = furtherElementsToMoveOffset
                    }
                    currentIndexOffset = null
                    editEventsInARow = 0
                }
                resultEditEvents.add(elementComparisonStrategy.update(indexOffset, oldElement, element))
                originalElements.removeAt(oldIndex)
                originalElements.add(index, oldElement)
            } else {
                if (currentIndexOffset != null && abs(currentIndexOffset) < editEventsInARow) {
                    resultEditEvents = removeLastEventsInCaseOnlyPositionChanged(resultEditEvents, editEventsInARow, elementComparisonStrategy).toMutableList()
                    val elementsToMove = abs(currentIndexOffset)
                    furtherElementsToMove = elementsToMove - 1 // we already move the current one
                    furtherElementsToMoveOffset = editEventsInARow + elementsToMove - 1 // we need to skip over further elements we need to move
                    resultEditEvents.add(elementComparisonStrategy.update(furtherElementsToMoveOffset, oldElement, element))
                }
                currentIndexOffset = null
                editEventsInARow = 0
            }
        }

        val allElements = newOrganizationElements.toMutableList()
        return listOf(
            resultDelete.map { elementComparisonStrategy.delete(it) },
            resultEditEvents,
            resultAdd.map { elementComparisonStrategy.create(allElements.indexOf(it), it) }
        ).flatten()
    }

    private fun <T, E : OrganizationEvent> removeLastEventsInCaseOnlyPositionChanged(events: List<E>, editEventsInARow: Int, elementComparisonStrategy: ElementComparisonStrategy<T, E>): List<E> {
        val resultEditEvents = events.toMutableList()
        val elements = resultEditEvents.size
        for (i in (elements - 1) downTo (elements - editEventsInARow)) {
            if (elementComparisonStrategy.isUpdateContent(resultEditEvents[i])) {
                resultEditEvents.add(i + 1, elementComparisonStrategy.updateWithChangedIndex(0, resultEditEvents[i]))
            }
            resultEditEvents.removeAt(i)
        }
        return resultEditEvents
    }

    companion object {

        private fun <T> getDiff(list1: List<T>, list2: List<T>): List<Pair<T, Int>> {
            val list = ArrayList(list1)
            for (e in list2) {
                val indexOf = list.indexOf(e)
                if (indexOf != -1) {
                    list.removeAt(indexOf)
                }
            }
            return list.map { Pair(it, list1.indexOf(it)) }
        }

        var metric: StringMetric = StringMetricBuilder.with(CosineSimilarity())
            .simplify(Simplifiers.toLowerCase(Locale.GERMAN))
            .simplify(Simplifiers.replaceNonWord())
            .tokenize(Tokenizers.whitespace())
            .build()
    }

}

interface ElementComparisonStrategy<T, E : OrganizationEvent> {
    fun create(index: Int, element: T): OrganizationEvent
    fun update(indexOffset: Int, oldElement: T, element: T): E
    fun delete(element: T): OrganizationEvent
    fun isUpdateContent(event: E): Boolean
    fun updateWithChangedIndex(indexOffset: Int, event: E): E
    fun isSimilar(element1: T, element2: T): Boolean = element1 == element2
    fun compareWithMetric(element1: T, element2: T): Float = 0.0f
}