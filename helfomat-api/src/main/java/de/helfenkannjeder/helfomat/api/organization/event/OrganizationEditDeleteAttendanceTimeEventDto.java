package de.helfenkannjeder.helfomat.api.organization.event;

import de.helfenkannjeder.helfomat.api.organization.AttendanceTimeDto;
import de.helfenkannjeder.helfomat.core.organization.OrganizationId;

/**
 * @author Valentin Zickner
 */
public class OrganizationEditDeleteAttendanceTimeEventDto extends OrganizationEventDto {
    private AttendanceTimeDto attendanceTime;

    OrganizationEditDeleteAttendanceTimeEventDto() {
    }

    public OrganizationEditDeleteAttendanceTimeEventDto(OrganizationId organizationId, AttendanceTimeDto attendanceTime) {
        super(organizationId);
        this.attendanceTime = attendanceTime;
    }

    public AttendanceTimeDto getAttendanceTime() {
        return attendanceTime;
    }

    @Override
    public <T> T visit(OrganizationEventDtoVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
