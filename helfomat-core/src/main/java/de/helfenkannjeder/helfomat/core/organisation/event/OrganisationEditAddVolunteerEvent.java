package de.helfenkannjeder.helfomat.core.organisation.event;

import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;
import de.helfenkannjeder.helfomat.core.organisation.Volunteer;

/**
 * @author Valentin Zickner
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
public class OrganisationEditAddVolunteerEvent extends OrganisationEditEvent {
    private int index;
    private Volunteer volunteer;

    public OrganisationEditAddVolunteerEvent(OrganisationId organisationId, int index, Volunteer volunteer) {
        super(organisationId);
        this.index = index;
        this.volunteer = volunteer;
    }

    @Override
    public Organisation.Builder applyOnOrganisationBuilder(Organisation.Builder organisation) {
        return organisation.addVolunteer(index, volunteer);
    }
}