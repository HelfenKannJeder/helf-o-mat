package de.helfenkannjeder.helfomat.api.organization

import com.google.common.base.Objects
import de.helfenkannjeder.helfomat.api.question.AnsweredQuestionDto
import de.helfenkannjeder.helfomat.core.organization.*
import de.helfenkannjeder.helfomat.core.picture.PictureId
import de.helfenkannjeder.helfomat.core.question.Question
import de.helfenkannjeder.helfomat.core.question.QuestionId

/**
 * @author Valentin Zickner
 */
object OrganizationAssembler {

    fun toScoredOrganizationDtos(organizations: List<ScoredOrganization>) = organizations.map { toOrganizationDto(it) }

    private fun toOrganizationDto(scoredOrganization: ScoredOrganization): OrganizationDto {
        val organization = scoredOrganization.organization
        return OrganizationDto(
            organization.id.value,
            organization.name,
            organization.urlName,
            organization.description,
            organization.website,
            toAddressDtos(organization.addresses),
            toContactPersonDtos(organization.contactPersons),
            organization.logo,
            scoredOrganization.score,
            organization.organizationType
        )
    }

    fun toOrganizationDtos(organizations: List<Organization>) = organizations.map { toOrganizationDto(it) }

    private fun toOrganizationDto(organization: Organization) = OrganizationDto(
        organization.id.value,
        organization.name,
        organization.urlName,
        organization.description,
        organization.website,
        toAddressDtos(organization.addresses),
        toContactPersonDtos(organization.contactPersons),
        organization.logo,
        null,
        organization.organizationType
    )

    fun toOrganizationDetailsDto(organizations: List<Organization>, questions: List<Question>) = organizations
        .map { toOrganizationDetailDto(it, questions)!! }

    fun toOrganizationDetailDto(organization: Organization?, questions: List<Question>) = if (organization == null) null
    else OrganizationDetailDto(
        organization.id.value,
        organization.name,
        organization.urlName,
        organization.organizationType,
        organization.description,
        organization.website,
        organization.logo,
        organization.pictures ?: emptyList(),
        toContactPersonDtos(organization.contactPersons),
        toAddressDto(organization.defaultAddress),
        toAddressDtos(organization.addresses),
        toAnsweredQuestionDtos(organization.questionAnswers, questions),
        organization.mapPin,
        toGroupDtos(organization.groups),
        toAttendanceTimeDtos(organization.attendanceTimes),
        toVolunteerDtos(organization.volunteers)
    )

    private fun toVolunteerDtos(volunteers: List<Volunteer>?) = volunteers?.map { toVolunteerDto(it) } ?: emptyList()

    @JvmStatic
    fun toVolunteerDto(volunteer: Volunteer) = VolunteerDto(
        volunteer.firstname,
        volunteer.motivation,
        volunteer.picture
    )

    private fun toAttendanceTimeDtos(attendanceTimes: List<AttendanceTime>?) =
        attendanceTimes?.map { toAttendanceTimeDto(it) } ?: emptyList()

    @JvmStatic
    fun toAttendanceTimeDto(attendanceTime: AttendanceTime) = AttendanceTimeDto(
        attendanceTime.day,
        attendanceTime.start,
        attendanceTime.end,
        attendanceTime.note,
        toGroupDtos(attendanceTime.groups)
    )

    private fun toAnsweredQuestionDtos(questionAnswers: List<QuestionAnswer>?, questions: List<Question>) =
        questionAnswers?.map { toAnsweredQuestionDto(it, determineQuestionText(questions, it.questionId)) }
            ?: emptyList()


    @JvmStatic
    fun toAnsweredQuestionDto(questionAnswer: QuestionAnswer, question: String?) = AnsweredQuestionDto(
        questionAnswer.questionId,
        question,
        questionAnswer.answer
    )

    @JvmStatic
    fun determineQuestionText(questions: List<Question>, id: QuestionId?) =
        questions.filter { question: Question -> Objects.equal(question.id, id) }
            .map { obj: Question -> obj.question }
            .firstOrNull()

    private fun toGroupDtos(groups: List<Group>?) = groups?.map { toGroupDto(it) } ?: emptyList()

    @JvmStatic
    fun toGroupDto(group: Group) = GroupDto(
        group.name,
        group.description
    )

    private fun toContactPersonDtos(contactPersons: List<ContactPerson>?) = contactPersons
        ?.map { toContactPersonDto(it) } ?: emptyList()

    @JvmStatic
    fun toContactPersonDto(contactPerson: ContactPerson) = ContactPersonDto(
        contactPerson.firstname,
        contactPerson.lastname,
        contactPerson.rank,
        contactPerson.telephone,
        contactPerson.mail,
        contactPerson.picture
    )

    private fun toAddressDtos(addresses: List<Address>?) = addresses?.map { toAddressDto(it) } ?: emptyList()

    @JvmStatic
    fun toAddressDto(address: Address?): AddressDto? = when (address) {
        null -> null
        else -> AddressDto(
            address.street,
            address.addressAppendix,
            address.city,
            address.zipcode,
            address.location,
            address.telephone,
            address.website
        )
    }

    fun toOrganizations(organizations: List<OrganizationDetailDto>): List<Organization> = organizations.map { toOrganization(it) }

    fun toOrganization(organizationDetailDto: OrganizationDetailDto) = Organization.Builder()
        .setId(OrganizationId(organizationDetailDto.id))
        .setName(organizationDetailDto.name)
        .setUrlName(organizationDetailDto.urlName)
        .setDescription(organizationDetailDto.description)
        .setWebsite(organizationDetailDto.website)
        .setLogo(organizationDetailDto.logo)
        .setGroups(toGroups(organizationDetailDto.groups))
        .setPictures(organizationDetailDto.pictures)
        .setDefaultAddress(toAddress(organizationDetailDto.defaultAddress))
        .setAddresses(toAddresses(organizationDetailDto.addresses))
        .setOrganizationType(organizationDetailDto.organizationType)
        .setAttendanceTimes(toAttendanceTimes(organizationDetailDto.attendanceTimes))
        .setMapPin(organizationDetailDto.mapPin)
        .setContactPersons(toContactPersons(organizationDetailDto.contactPersons))
        .setQuestionAnswers(toQuestionAnswers(organizationDetailDto.questions))
        .setTeaserImage(toTeaserImage(organizationDetailDto.pictures))
        .setVolunteers(toVolunteers(organizationDetailDto.volunteers))
        .build()

    private fun toVolunteers(volunteers: List<VolunteerDto>?) = volunteers?.map { toVolunteer(it) } ?: emptyList()

    @JvmStatic
    fun toVolunteer(volunteerDto: VolunteerDto) = Volunteer.Builder()
        .setFirstname(volunteerDto.firstname)
        .setMotivation(volunteerDto.motivation)
        .setPicture(volunteerDto.picture)
        .build()

    private fun toTeaserImage(pictures: List<PictureId>?) = pictures?.firstOrNull()

    private fun toQuestionAnswers(questions: List<AnsweredQuestionDto>?) = questions?.map { toQuestionAnswer(it) }

    @JvmStatic
    fun toQuestionAnswer(answeredQuestionDto: AnsweredQuestionDto) = QuestionAnswer(answeredQuestionDto.questionId, answeredQuestionDto.answer)

    private fun toContactPersons(contactPersons: List<ContactPersonDto>?) = contactPersons?.map { toContactPerson(it) }

    @JvmStatic
    fun toContactPerson(contactPersonDto: ContactPersonDto) = ContactPerson.Builder()
        .setFirstname(contactPersonDto.firstname)
        .setLastname(contactPersonDto.lastname)
        .setPicture(contactPersonDto.picture)
        .setRank(contactPersonDto.rank)
        .setTelephone(contactPersonDto.telephone)
        .setMail(contactPersonDto.mail)
        .build()

    private fun toAttendanceTimes(attendanceTimes: List<AttendanceTimeDto>?) = attendanceTimes?.map { toAttendanceTime(it) }
        ?: emptyList()

    @JvmStatic
    fun toAttendanceTime(attendanceTimeDto: AttendanceTimeDto) = AttendanceTime.Builder()
        .setDay(attendanceTimeDto.day)
        .setStart(attendanceTimeDto.start)
        .setEnd(attendanceTimeDto.end)
        .setNote(attendanceTimeDto.note)
        .setGroups(toGroups(attendanceTimeDto.groups))
        .build()

    private fun toGroups(groups: List<GroupDto>?) = groups?.map { toGroup(it) }

    @JvmStatic
    fun toGroup(groupDto: GroupDto): Group {
        return Group.Builder()
            .setName(groupDto.name)
            .setDescription(groupDto.description)
            .build()
    }

    private fun toAddresses(addresses: List<AddressDto>?) = addresses?.map { toAddress(it)!! }

    @JvmStatic
    fun toAddress(addressDto: AddressDto?) = when (addressDto) {
        null -> null
        else -> Address.Builder()
            .setLocation(addressDto.location)
            .setCity(addressDto.city)
            .setStreet(addressDto.street)
            .setZipcode(addressDto.zipcode)
            .setAddressAppendix(addressDto.addressAppendix)
            .setTelephone(addressDto.telephone)
            .setWebsite(addressDto.website)
            .build()
    }
}