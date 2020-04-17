package de.helfenkannjeder.helfomat.core.organization.event;

import de.helfenkannjeder.helfomat.core.organization.AttendanceTime;
import de.helfenkannjeder.helfomat.core.organization.Organization;
import de.helfenkannjeder.helfomat.core.organization.OrganizationEventVisitor;
import de.helfenkannjeder.helfomat.core.organization.OrganizationId;

/**
 * @author Valentin Zickner
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
public class OrganizationEditDeleteAttendanceTimeEvent extends OrganizationEditEvent {
    private AttendanceTime attendanceTime;

    protected OrganizationEditDeleteAttendanceTimeEvent() {
    }

    public OrganizationEditDeleteAttendanceTimeEvent(OrganizationId organizationId, AttendanceTime attendanceTime) {
        super(organizationId);
        this.attendanceTime = attendanceTime;
    }

    public AttendanceTime getAttendanceTime() {
        return attendanceTime;
    }

    @Override
    public Organization.Builder applyOnOrganizationBuilder(Organization.Builder organization) {
        return organization.removeAttendanceTime(attendanceTime);
    }

    @Override
    public <T> T visit(OrganizationEventVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
