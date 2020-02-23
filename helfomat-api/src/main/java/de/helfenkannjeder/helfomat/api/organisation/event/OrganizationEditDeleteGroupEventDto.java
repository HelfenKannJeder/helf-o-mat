package de.helfenkannjeder.helfomat.api.organisation.event;

import de.helfenkannjeder.helfomat.api.organisation.GroupDto;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;

/**
 * @author Valentin Zickner
 */
public class OrganizationEditDeleteGroupEventDto extends OrganizationEventDto {
    private GroupDto group;

    OrganizationEditDeleteGroupEventDto() {
    }

    public OrganizationEditDeleteGroupEventDto(OrganisationId organisationId, GroupDto group) {
        super(organisationId);
        this.group = group;
    }

    public GroupDto getGroup() {
        return group;
    }
}
