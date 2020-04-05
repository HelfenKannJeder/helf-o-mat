package de.helfenkannjeder.helfomat.api.organisation.event;

import de.helfenkannjeder.helfomat.api.organisation.AttendanceTimeDto;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;

/**
 * @author Valentin Zickner
 */
public class OrganizationEditAddAttendanceTimeEventDto extends OrganizationEventDto {
    private int index;
    private AttendanceTimeDto attendanceTime;

    OrganizationEditAddAttendanceTimeEventDto() {
    }

    public OrganizationEditAddAttendanceTimeEventDto(OrganisationId organisationId, int index, AttendanceTimeDto attendanceTime) {
        super(organisationId);
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
