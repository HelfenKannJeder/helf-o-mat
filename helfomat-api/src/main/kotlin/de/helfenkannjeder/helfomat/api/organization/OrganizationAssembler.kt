package de.helfenkannjeder.helfomat.api.organization

import de.helfenkannjeder.helfomat.api.question.AnsweredQuestionDto
import de.helfenkannjeder.helfomat.core.organization.*
import de.helfenkannjeder.helfomat.core.question.Question
import de.helfenkannjeder.helfomat.core.question.QuestionId

/**
 * @author Valentin Zickner
 */

fun List<ScoredOrganization>.toScoredOrganizationDtos() = this.map { it.toOrganizationDto() }
fun ScoredOrganization.toOrganizationDto(): OrganizationDto {
    val scoredOrganization = this
    val organization = scoredOrganization.organization
    return OrganizationDto(
        organization.id.value,
        organization.name,
        organization.urlName,
        organization.description,
        organization.website,
        organization.defaultAddress?.toAddressDto(),
        organization.addresses.toAddressDtos(),
        organization.contactPersons.toContactPersonDtos(),
        organization.logo,
        scoredOrganization.score,
        organization.organizationType
    )
}

fun List<Organization>.toOrganizationDtos() = this.map { it.toOrganizationDto() }
fun Organization.toOrganizationDto() = OrganizationDto(
    this.id.value,
    this.name,
    this.urlName,
    this.description,
    this.website,
    this.defaultAddress?.toAddressDto(),
    this.addresses.toAddressDtos(),
    this.contactPersons.toContactPersonDtos(),
    this.logo,
    null,
    this.organizationType
)

fun List<Organization>.toOrganizationDetailsDto(questions: List<Question>) = this.map { it.toOrganizationDetailDto(questions) }
fun Organization.toOrganizationDetailDto(questions: List<Question>) = OrganizationDetailDto(
    this.id.value,
    this.name,
    this.urlName,
    this.organizationType,
    this.description,
    this.website,
    this.logo,
    this.pictures,
    this.contactPersons.toContactPersonDtos(),
    this.defaultAddress?.toAddressDto(),
    this.addresses.toAddressDtos(),
    this.questionAnswers.toAnsweredQuestionDtos(questions),
    this.mapPin,
    this.groups.toGroupDtos(),
    this.attendanceTimes.toAttendanceTimeDtos(),
    this.volunteers.toVolunteerDtos()
)

fun List<Volunteer>.toVolunteerDtos() = this.map { it.toVolunteerDto() }
fun Volunteer.toVolunteerDto() = VolunteerDto(this.firstname, this.motivation, this.picture)

fun List<AttendanceTime>.toAttendanceTimeDtos() = this.map { it.toAttendanceTimeDto() }
fun AttendanceTime.toAttendanceTimeDto() = AttendanceTimeDto(this.day, this.start, this.end, this.note, this.groups.toGroupDtos())

fun List<QuestionAnswer>.toAnsweredQuestionDtos(questions: List<Question>) = this.map { it.toAnsweredQuestionDto(questions.getAnswerToQuestion(it.questionId)) }
fun QuestionAnswer.toAnsweredQuestionDto(question: String) = AnsweredQuestionDto(this.questionId, question, this.answer)
fun List<Question>.getAnswerToQuestion(id: QuestionId?) =
    this.filter { question: Question -> question.id == id }
        .map { obj: Question -> obj.question }
        .first()

fun List<Group>.toGroupDtos() = this.map { it.toGroupDto() }
fun Group.toGroupDto() = GroupDto(this.name, this.description)

fun List<ContactPerson>.toContactPersonDtos() = this.map { it.toContactPersonDto() }
fun ContactPerson.toContactPersonDto() = ContactPersonDto(this.firstname, this.lastname, this.rank, this.telephone, this.mail, this.picture)

fun List<Address>.toAddressDtos() = this.map { it.toAddressDto() }
fun Address.toAddressDto(): AddressDto = AddressDto(this.street, this.addressAppendix, this.city, this.zipcode, this.location, this.telephone, this.website)

fun List<OrganizationDetailDto>.toOrganizations(): List<Organization> = this.map { it.toOrganization() }
fun OrganizationDetailDto.toOrganization() = Organization.Builder(
    id = OrganizationId(this.id),
    name = this.name,
    urlName = this.urlName,
    organizationType = this.organizationType
)
    .setDescription(this.description)
    .setWebsite(this.website)
    .setLogo(this.logo)
    .setGroups(this.groups.toGroups())
    .setPictures(this.pictures)
    .setDefaultAddress(this.defaultAddress?.toAddress())
    .setAddresses(this.addresses.toAddresses())
    .setAttendanceTimes(this.attendanceTimes.toAttendanceTimes())
    .setMapPin(this.mapPin)
    .setContactPersons(this.contactPersons.toContactPersons())
    .setQuestionAnswers(this.questions.toQuestionAnswers())
    .setTeaserImage(this.pictures.firstOrNull())
    .setVolunteers(this.volunteers.toVolunteers())
    .build()

fun List<VolunteerDto>.toVolunteers() = this.map { it.toVolunteer() }
fun VolunteerDto.toVolunteer() = Volunteer(firstname = this.firstname, motivation = this.motivation, picture = this.picture)

fun List<AnsweredQuestionDto>.toQuestionAnswers() = this.map { it.toQuestionAnswer() }
fun AnsweredQuestionDto.toQuestionAnswer() = QuestionAnswer(this.questionId, this.answer)

fun List<ContactPersonDto>.toContactPersons() = this.map { it.toContactPerson() }
fun ContactPersonDto.toContactPerson() = ContactPerson(this.firstname, this.lastname, this.rank, this.telephone, this.mail, this.picture)

fun List<AttendanceTimeDto>.toAttendanceTimes() = this.map { it.toAttendanceTime() }
fun AttendanceTimeDto.toAttendanceTime() = AttendanceTime(this.day, this.start, this.end, this.note, this.groups.toGroups())

fun List<GroupDto>.toGroups() = this.map { it.toGroup() }
fun GroupDto.toGroup() = Group(name = this.name, description = this.description)

fun List<AddressDto>.toAddresses() = this.map { it.toAddress() }
fun AddressDto.toAddress() = Address(this.street, this.addressAppendix, this.city, this.zipcode, this.location, this.telephone, this.website)
