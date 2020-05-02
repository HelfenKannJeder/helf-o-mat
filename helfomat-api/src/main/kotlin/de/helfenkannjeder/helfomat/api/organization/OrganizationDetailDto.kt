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
    val description: String?,
    val website: String?,
    val logo: PictureId,
    val pictures: List<PictureId>,
    val contactPersons: List<ContactPersonDto>,
    val defaultAddress: AddressDto?,
    val addresses: List<AddressDto>,
    val questions: List<AnsweredQuestionDto>,
    val mapPin: String?,
    val groups: List<GroupDto>,
    val attendanceTimes: List<AttendanceTimeDto>,
    val volunteers: List<VolunteerDto>
)