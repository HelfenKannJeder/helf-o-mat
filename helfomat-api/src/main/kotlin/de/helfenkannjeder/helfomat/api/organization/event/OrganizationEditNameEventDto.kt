package de.helfenkannjeder.helfomat.api.organization.event;

import de.helfenkannjeder.helfomat.core.organization.OrganizationId;

/**
 * @author Valentin Zickner
 */
public class OrganizationEditNameEventDto extends OrganizationEventDto {
    private String name;

    OrganizationEditNameEventDto() {
    }

    public OrganizationEditNameEventDto(OrganizationId organizationId, String name) {
        super(organizationId);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public <T> T visit(OrganizationEventDtoVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
