package de.helfenkannjeder.helfomat.core.organization.event

import de.helfenkannjeder.helfomat.core.organization.AttendanceTime
import de.helfenkannjeder.helfomat.core.organization.Organization
import de.helfenkannjeder.helfomat.core.organization.OrganizationEventVisitor
import de.helfenkannjeder.helfomat.core.organization.OrganizationId

/**
 * @author Valentin Zickner
 */
data class OrganizationEditAddAttendanceTimeEvent(
    override val organizationId: OrganizationId,
    val index: Int,
    val attendanceTime: AttendanceTime
) : OrganizationEditEvent(organizationId) {

    override fun applyOnOrganizationBuilder(organizationBuilder: Organization.Builder?, strictMode: Boolean): Organization.Builder? {
        if (organizationBuilder?.attendanceTimes != null) {
            if (organizationBuilder.attendanceTimes.size > index) {
                organizationBuilder.attendanceTimes.add(index, attendanceTime)
            } else {
                organizationBuilder.attendanceTimes.add(attendanceTime)
            }
        }
        return organizationBuilder
    }

    override fun <T> visit(visitor: OrganizationEventVisitor<T>): T {
        return visitor.visit(this)
    }

}