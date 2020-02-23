package de.helfenkannjeder.helfomat.api.organisation.event;

import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;

/**
 * @author Valentin Zickner
 */
public class OrganizationEditNameEventDto extends OrganizationEventDto {
    private String name;

    OrganizationEditNameEventDto() {
    }

    public OrganizationEditNameEventDto(OrganisationId organisationId, String name) {
        super(organisationId);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
