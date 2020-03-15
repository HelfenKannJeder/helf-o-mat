package de.helfenkannjeder.helfomat.api.organisation.event;

import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;

/**
 * @author Valentin Zickner
 */
public class OrganizationDeleteEventDto extends OrganizationEventDto {

    OrganizationDeleteEventDto() {
    }

    public OrganizationDeleteEventDto(OrganisationId organisationId) {
        super(organisationId);
    }

    @Override
    public <T> T visit(OrganizationEventDtoVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
