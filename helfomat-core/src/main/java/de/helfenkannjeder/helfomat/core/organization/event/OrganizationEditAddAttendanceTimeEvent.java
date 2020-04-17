package de.helfenkannjeder.helfomat.core.organization.event;

import de.helfenkannjeder.helfomat.core.organization.AttendanceTime;
import de.helfenkannjeder.helfomat.core.organization.Organization;
import de.helfenkannjeder.helfomat.core.organization.OrganizationEventVisitor;
import de.helfenkannjeder.helfomat.core.organization.OrganizationId;

/**
 * @author Valentin Zickner
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
public class OrganizationEditAddAttendanceTimeEvent extends OrganizationEditEvent {
    private int index;
    private AttendanceTime attendanceTime;

    protected OrganizationEditAddAttendanceTimeEvent() {
    }

    public OrganizationEditAddAttendanceTimeEvent(OrganizationId organizationId, int index, AttendanceTime attendanceTime) {
        super(organizationId);
        this.index = index;
        this.attendanceTime = attendanceTime;
    }

    public int getIndex() {
        return index;
    }

    public AttendanceTime getAttendanceTime() {
        return attendanceTime;
    }

    @Override
    public Organization.Builder applyOnOrganizationBuilder(Organization.Builder organization) {
        return organization.addAttendanceTime(index, attendanceTime);
    }

    @Override
    public <T> T visit(OrganizationEventVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
