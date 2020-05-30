package de.helfenkannjeder.helfomat.api.organization

import de.helfenkannjeder.helfomat.api.question.AnsweredQuestionDto
import de.helfenkannjeder.helfomat.core.organization.OrganizationType
import de.helfenkannjeder.helfomat.core.picture.PictureId

/**
 * @author Valentin Zickner
 */
data class OrganizationDetailDto(
    val id: String,
    val name: String,
    val urlName: String,
    val organizationType: OrganizationType,
    val description: String? = null,
    val website: String? = null,
    val logo: PictureId? = null,
    val pictures: List<PictureId> = arrayListOf(),
    val contactPersons: List<ContactPersonDto> = arrayListOf(),
    val defaultAddress: AddressDto? = null,
    val addresses: List<AddressDto> = arrayListOf(),
    val questions: List<AnsweredQuestionDto> = arrayListOf(),
    val mapPin: String? = null,
    val groups: List<GroupDto> = arrayListOf(),
    val attendanceTimes: List<AttendanceTimeDto> = arrayListOf(),
    val volunteers: List<VolunteerDto> = arrayListOf()
)