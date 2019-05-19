package de.helfenkannjeder.helfomat.core.organisation.event;

import de.helfenkannjeder.helfomat.core.organisation.AttendanceTime;
import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;

/**
 * @author Valentin Zickner
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
public class OrganisationEditAddAttendanceTimeEvent extends OrganisationEditEvent {
    private int index;
    private AttendanceTime attendanceTime;

    protected OrganisationEditAddAttendanceTimeEvent() {
    }

    public OrganisationEditAddAttendanceTimeEvent(OrganisationId organisationId, int index, AttendanceTime attendanceTime) {
        super(organisationId);
        this.index = index;
        this.attendanceTime = attendanceTime;
    }

    @Override
    public Organisation.Builder applyOnOrganisationBuilder(Organisation.Builder organisation) {
        return organisation.addAttendanceTime(index, attendanceTime);
    }
}
