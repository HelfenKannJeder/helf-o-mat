package de.helfenkannjeder.helfomat.api.organisation.event;

import de.helfenkannjeder.helfomat.api.organisation.VolunteerDto;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;

/**
 * @author Valentin Zickner
 */
public class OrganizationEditDeleteVolunteerEventDto extends OrganizationEventDto {
    private VolunteerDto volunteer;

    OrganizationEditDeleteVolunteerEventDto() {
    }

    public OrganizationEditDeleteVolunteerEventDto(OrganisationId organisationId, VolunteerDto volunteer) {
        super(organisationId);
        this.volunteer = volunteer;
    }

    public VolunteerDto getVolunteer() {
        return volunteer;
    }
}
