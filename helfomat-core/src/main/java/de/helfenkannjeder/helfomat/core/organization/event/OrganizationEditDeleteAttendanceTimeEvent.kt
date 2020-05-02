package de.helfenkannjeder.helfomat.core.organization.event

import de.helfenkannjeder.helfomat.core.organization.AttendanceTime
import de.helfenkannjeder.helfomat.core.organization.Organization
import de.helfenkannjeder.helfomat.core.organization.OrganizationEventVisitor
import de.helfenkannjeder.helfomat.core.organization.OrganizationId

/**
 * @author Valentin Zickner
 */
data class OrganizationEditDeleteAttendanceTimeEvent(
    override val organizationId: OrganizationId,
    val attendanceTime: AttendanceTime
) : OrganizationEditEvent(organizationId) {

    override fun applyOnOrganizationBuilder(organization: Organization.Builder?): Organization.Builder? {
        organization?.attendanceTimes?.remove(attendanceTime)
        return organization
    }

    override fun <T> visit(visitor: OrganizationEventVisitor<T>): T {
        return visitor.visit(this)
    }

}