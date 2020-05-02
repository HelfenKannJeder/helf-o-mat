package de.helfenkannjeder.helfomat.api.organization.event;

import de.helfenkannjeder.helfomat.api.organization.AttendanceTimeDto;
import de.helfenkannjeder.helfomat.core.organization.OrganizationId;

/**
 * @author Valentin Zickner
 */
public class OrganizationEditAddAttendanceTimeEventDto extends OrganizationEventDto {
    private int index;
    private AttendanceTimeDto attendanceTime;

    OrganizationEditAddAttendanceTimeEventDto() {
    }

    public OrganizationEditAddAttendanceTimeEventDto(OrganizationId organizationId, int index, AttendanceTimeDto attendanceTime) {
        super(organizationId);
        this.index = index;
        this.attendanceTime = attendanceTime;
    }

    public int getIndex() {
        return index;
    }

    public AttendanceTimeDto getAttendanceTime() {
        return attendanceTime;
    }

    @Override
    public <T> T visit(OrganizationEventDtoVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
