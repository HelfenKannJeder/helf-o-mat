package de.helfenkannjeder.helfomat.api.organization.event;

import de.helfenkannjeder.helfomat.api.organization.GroupDto;
import de.helfenkannjeder.helfomat.core.organization.OrganizationId;

/**
 * @author Valentin Zickner
 */
public class OrganizationEditDeleteGroupEventDto extends OrganizationEventDto {
    private GroupDto group;

    OrganizationEditDeleteGroupEventDto() {
    }

    public OrganizationEditDeleteGroupEventDto(OrganizationId organizationId, GroupDto group) {
        super(organizationId);
        this.group = group;
    }

    public GroupDto getGroup() {
        return group;
    }

    @Override
    public <T> T visit(OrganizationEventDtoVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
