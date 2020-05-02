package de.helfenkannjeder.helfomat.api.organization.event;

import de.helfenkannjeder.helfomat.core.organization.OrganizationId;

/**
 * @author Valentin Zickner
 */
public class OrganizationEditDescriptionEventDto extends OrganizationEventDto {
    private String description;

    OrganizationEditDescriptionEventDto() {
    }

    public OrganizationEditDescriptionEventDto(OrganizationId organizationId, String description) {
        super(organizationId);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public <T> T visit(OrganizationEventDtoVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
