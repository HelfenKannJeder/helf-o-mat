package de.helfenkannjeder.helfomat.api.organization.event

import de.helfenkannjeder.helfomat.api.organization.AttendanceTimeDto
import de.helfenkannjeder.helfomat.core.organization.OrganizationId

data class OrganizationEditChangeAttendanceTimeEventDto(
    override val organizationId: OrganizationId,
    val indexOffset: Int,
    val oldAttendanceTime: AttendanceTimeDto,
    val attendanceTime: AttendanceTimeDto,
    override val eventApplicable: Boolean
) : OrganizationEventDto {

    override fun <T> visit(visitor: OrganizationEventDtoVisitor<T>): T = visitor.visit(this)

}
