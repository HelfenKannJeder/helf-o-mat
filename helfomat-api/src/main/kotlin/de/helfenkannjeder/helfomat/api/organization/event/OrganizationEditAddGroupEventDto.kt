package de.helfenkannjeder.helfomat.api.organization.event;

import de.helfenkannjeder.helfomat.api.organization.GroupDto;
import de.helfenkannjeder.helfomat.core.organization.OrganizationId;

/**
 * @author Valentin Zickner
 */
public class OrganizationEditAddGroupEventDto extends OrganizationEventDto {
    private int index;
    private GroupDto group;

    OrganizationEditAddGroupEventDto() {
    }

    public OrganizationEditAddGroupEventDto(OrganizationId organizationId, int index, GroupDto group) {
        super(organizationId);
        this.index = index;
        this.group = group;
    }

    public int getIndex() {
        return index;
    }

    public GroupDto getGroup() {
        return group;
    }

    @Override
    public <T> T visit(OrganizationEventDtoVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
