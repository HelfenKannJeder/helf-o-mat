package de.helfenkannjeder.helfomat.api.organisation.event;

import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;

/**
 * @author Valentin Zickner
 */
public class OrganizationEditWebsiteEventDto extends OrganizationEventDto {
    private String website;

    OrganizationEditWebsiteEventDto() {
    }

    public OrganizationEditWebsiteEventDto(OrganisationId organisationId, String website) {
        super(organisationId);
        this.website = website;
    }

    public String getWebsite() {
        return website;
    }
}
