package de.helfenkannjeder.helfomat.api.organisation.event;

import de.helfenkannjeder.helfomat.api.organisation.GroupDto;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;

/**
 * @author Valentin Zickner
 */
public class OrganizationEditAddGroupEventDto extends OrganizationEventDto {
    private int index;
    private GroupDto group;

    OrganizationEditAddGroupEventDto() {
    }

    public OrganizationEditAddGroupEventDto(OrganisationId organisationId, int index, GroupDto group) {
        super(organisationId);
        this.index = index;
        this.group = group;
    }

    public int getIndex() {
        return index;
    }

    public GroupDto getGroup() {
        return group;
    }
}
