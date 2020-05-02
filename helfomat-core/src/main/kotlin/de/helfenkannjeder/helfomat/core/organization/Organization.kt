package de.helfenkannjeder.helfomat.core.organization

import de.helfenkannjeder.helfomat.core.organization.event.*
import de.helfenkannjeder.helfomat.core.picture.PictureId

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
        differences.addAll(getDiff(organization.pictures, pictures).map { OrganizationEditDeletePictureEvent(id, it.first) })
        differences.addAll(getDiff(pictures, organization.pictures).map { OrganizationEditAddPictureEvent(id, it.second, it.first) })
        differences.addAll(getDiff(organization.contactPersons, contactPersons).map { OrganizationEditDeleteContactPersonEvent(id, it.first) })
        differences.addAll(getDiff(contactPersons, organization.contactPersons).map { OrganizationEditAddContactPersonEvent(id, it.second, it.first) })
        differences.addAll(getDiff(organization.addresses, addresses).map { OrganizationEditDeleteAddressEvent(id, it.first) })
        differences.addAll(getDiff(addresses, organization.addresses).map { OrganizationEditAddAddressEvent(id, it.second, it.first) })
        differences.addAll(getDiff(organization.questionAnswers, questionAnswers).map { OrganizationEditDeleteQuestionAnswerEvent(id, it.first) })
        differences.addAll(getDiff(questionAnswers, organization.questionAnswers).map { OrganizationEditAddQuestionAnswerEvent(id, it.second, it.first) })
        differences.addAll(getDiff(organization.groups, groups).map { OrganizationEditDeleteGroupEvent(id, it.first) })
        differences.addAll(getDiff(groups, organization.groups).map { OrganizationEditAddGroupEvent(id, it.second, it.first) })
        differences.addAll(getDiff(organization.attendanceTimes, attendanceTimes).map { OrganizationEditDeleteAttendanceTimeEvent(id, it.first) })
        differences.addAll(getDiff(attendanceTimes, organization.attendanceTimes).map { OrganizationEditAddAttendanceTimeEvent(id, it.second, it.first) })
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

    companion object {
        private fun <T> getDiff(list1: List<T>, list2: List<T>): List<Pair<T, Int>> {
            val list = ArrayList(list1)
            list.removeAll(list2)
            return list.map { Pair(it, list1.indexOf(it)) }
        }
    }

}