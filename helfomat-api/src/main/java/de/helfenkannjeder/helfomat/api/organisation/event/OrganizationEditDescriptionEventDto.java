package de.helfenkannjeder.helfomat.api.organisation.event;

import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;

/**
 * @author Valentin Zickner
 */
public class OrganizationEditDescriptionEventDto extends OrganizationEventDto {
    private String description;

    OrganizationEditDescriptionEventDto() {
    }

    public OrganizationEditDescriptionEventDto(OrganisationId organisationId, String description) {
        super(organisationId);
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
