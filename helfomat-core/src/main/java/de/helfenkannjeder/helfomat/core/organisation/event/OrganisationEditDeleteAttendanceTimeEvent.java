package de.helfenkannjeder.helfomat.core.organisation.event;

import de.helfenkannjeder.helfomat.core.organisation.AttendanceTime;
import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;

/**
 * @author Valentin Zickner
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
public class OrganisationEditDeleteAttendanceTimeEvent extends OrganisationEditEvent {
    private AttendanceTime attendanceTime;

    protected OrganisationEditDeleteAttendanceTimeEvent() {
    }

    public OrganisationEditDeleteAttendanceTimeEvent(OrganisationId organisationId, AttendanceTime attendanceTime) {
        super(organisationId);
        this.attendanceTime = attendanceTime;
    }

    public AttendanceTime getAttendanceTime() {
        return attendanceTime;
    }

    @Override
    public Organisation.Builder applyOnOrganisationBuilder(Organisation.Builder organisation) {
        return organisation.removeAttendanceTime(attendanceTime);
    }
}
