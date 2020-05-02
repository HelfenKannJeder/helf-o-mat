package de.helfenkannjeder.helfomat.api.organization.event;

import de.helfenkannjeder.helfomat.core.organization.OrganizationId;

/**
 * @author Valentin Zickner
 */
public class OrganizationDeleteEventDto extends OrganizationEventDto {

    OrganizationDeleteEventDto() {
    }

    public OrganizationDeleteEventDto(OrganizationId organizationId) {
        super(organizationId);
    }

    @Override
    public <T> T visit(OrganizationEventDtoVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
