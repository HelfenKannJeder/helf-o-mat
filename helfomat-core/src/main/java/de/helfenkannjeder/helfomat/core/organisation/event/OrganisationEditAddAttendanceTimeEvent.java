package de.helfenkannjeder.helfomat.core.organisation.event;

import de.helfenkannjeder.helfomat.core.organisation.AttendanceTime;
import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;
import de.helfenkannjeder.helfomat.core.organisation.OrganizationEventVisitor;

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

    public int getIndex() {
        return index;
    }

    public AttendanceTime getAttendanceTime() {
        return attendanceTime;
    }

    @Override
    public Organisation.Builder applyOnOrganisationBuilder(Organisation.Builder organisation) {
        return organisation.addAttendanceTime(index, attendanceTime);
    }

    @Override
    public <T> T visit(OrganizationEventVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
