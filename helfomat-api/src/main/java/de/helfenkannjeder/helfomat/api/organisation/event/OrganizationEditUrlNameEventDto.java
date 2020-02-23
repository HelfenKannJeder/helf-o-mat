package de.helfenkannjeder.helfomat.api.organisation.event;

import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;

/**
 * @author Valentin Zickner
 */
public class OrganizationEditUrlNameEventDto extends OrganizationEventDto {
    private String urlName;

    OrganizationEditUrlNameEventDto() {
    }

    public OrganizationEditUrlNameEventDto(OrganisationId organisationId, String urlName) {
        super(organisationId);
        this.urlName = urlName;
    }

    public String getUrlName() {
        return urlName;
    }
}
