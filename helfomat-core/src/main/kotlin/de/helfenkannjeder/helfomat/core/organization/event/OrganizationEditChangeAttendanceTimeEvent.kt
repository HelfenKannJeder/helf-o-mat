package de.helfenkannjeder.helfomat.core.organization.event

import de.helfenkannjeder.helfomat.core.organization.AttendanceTime
import de.helfenkannjeder.helfomat.core.organization.Organization
import de.helfenkannjeder.helfomat.core.organization.OrganizationEventVisitor
import de.helfenkannjeder.helfomat.core.organization.OrganizationId

/**
 * @author Valentin Zickner
 */
data class OrganizationEditChangeAttendanceTimeEvent(
    override val organizationId: OrganizationId,
    val indexOffset: Int,
    val oldAttendanceTime: AttendanceTime,
    val attendanceTime: AttendanceTime
) : OrganizationEditEvent(organizationId) {

    override fun applyOnOrganizationBuilder(organizationBuilder: Organization.Builder?, strictMode: Boolean): Organization.Builder? {
        val attendanceTimes = organizationBuilder?.attendanceTimes ?: return organizationBuilder
        organizationBuilder.attendanceTimes = changePosition(attendanceTimes, oldAttendanceTime, attendanceTime, indexOffset)
        return organizationBuilder

    }

    override fun <T> visit(visitor: OrganizationEventVisitor<T>): T {
        return visitor.visit(this)
    }

}