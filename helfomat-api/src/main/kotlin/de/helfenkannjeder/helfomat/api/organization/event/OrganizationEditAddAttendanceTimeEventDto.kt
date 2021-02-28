package de.helfenkannjeder.helfomat.api.organization.event

import de.helfenkannjeder.helfomat.api.organization.AttendanceTimeDto
import de.helfenkannjeder.helfomat.core.organization.OrganizationId

/**
 * @author Valentin Zickner
 */
data class OrganizationEditAddAttendanceTimeEventDto(
    override val organizationId: OrganizationId,
    val index: Int,
    val attendanceTime: AttendanceTimeDto,
    override val eventApplicable: EventApplicability
) : OrganizationEventDto {

    override fun <T> visit(visitor: OrganizationEventDtoVisitor<T>): T {
        return visitor.visit(this)
    }

}