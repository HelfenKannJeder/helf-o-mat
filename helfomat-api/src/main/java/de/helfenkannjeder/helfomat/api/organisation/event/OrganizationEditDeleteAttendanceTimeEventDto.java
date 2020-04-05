package de.helfenkannjeder.helfomat.api.organisation.event;

import de.helfenkannjeder.helfomat.api.organisation.AttendanceTimeDto;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;

/**
 * @author Valentin Zickner
 */
public class OrganizationEditDeleteAttendanceTimeEventDto extends OrganizationEventDto {
    private AttendanceTimeDto attendanceTime;

    OrganizationEditDeleteAttendanceTimeEventDto() {
    }

    public OrganizationEditDeleteAttendanceTimeEventDto(OrganisationId organisationId, AttendanceTimeDto attendanceTime) {
        super(organisationId);
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
