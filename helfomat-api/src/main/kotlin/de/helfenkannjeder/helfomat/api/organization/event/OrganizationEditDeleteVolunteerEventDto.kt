package de.helfenkannjeder.helfomat.api.organization.event;

import de.helfenkannjeder.helfomat.api.organization.VolunteerDto;
import de.helfenkannjeder.helfomat.core.organization.OrganizationId;

/**
 * @author Valentin Zickner
 */
public class OrganizationEditDeleteVolunteerEventDto extends OrganizationEventDto {
    private VolunteerDto volunteer;

    OrganizationEditDeleteVolunteerEventDto() {
    }

    public OrganizationEditDeleteVolunteerEventDto(OrganizationId organizationId, VolunteerDto volunteer) {
        super(organizationId);
        this.volunteer = volunteer;
    }

    public VolunteerDto getVolunteer() {
        return volunteer;
    }

    @Override
    public <T> T visit(OrganizationEventDtoVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
