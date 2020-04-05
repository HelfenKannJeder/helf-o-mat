package de.helfenkannjeder.helfomat.api.organisation.event;

import de.helfenkannjeder.helfomat.api.organisation.VolunteerDto;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;

/**
 * @author Valentin Zickner
 */
public class OrganizationEditAddVolunteerEventDto extends OrganizationEventDto {
    private int index;
    private VolunteerDto volunteer;

    OrganizationEditAddVolunteerEventDto() {
    }

    public OrganizationEditAddVolunteerEventDto(OrganisationId organisationId, int index, VolunteerDto volunteer) {
        super(organisationId);
        this.index = index;
        this.volunteer = volunteer;
    }

    public int getIndex() {
        return index;
    }

    public VolunteerDto getVolunteer() {
        return volunteer;
    }

    @Override
    public <T> T visit(OrganizationEventDtoVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
