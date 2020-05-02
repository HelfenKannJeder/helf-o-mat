package de.helfenkannjeder.helfomat.api.organization.event;

import de.helfenkannjeder.helfomat.api.organization.VolunteerDto;
import de.helfenkannjeder.helfomat.core.organization.OrganizationId;

/**
 * @author Valentin Zickner
 */
public class OrganizationEditAddVolunteerEventDto extends OrganizationEventDto {
    private int index;
    private VolunteerDto volunteer;

    OrganizationEditAddVolunteerEventDto() {
    }

    public OrganizationEditAddVolunteerEventDto(OrganizationId organizationId, int index, VolunteerDto volunteer) {
        super(organizationId);
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
