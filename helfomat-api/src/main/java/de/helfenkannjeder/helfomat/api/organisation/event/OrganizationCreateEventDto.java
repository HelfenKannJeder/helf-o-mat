package de.helfenkannjeder.helfomat.api.organisation.event;

import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationType;

/**
 * @author Valentin Zickner
 */
public class OrganizationCreateEventDto extends OrganizationEventDto {

    private String name;
    private String urlName;
    private OrganisationType organisationType;

    OrganizationCreateEventDto() {
    }

    public OrganizationCreateEventDto(OrganisationId organisationId, String name, String urlName, OrganisationType organisationType) {
        super(organisationId);
        this.name = name;
        this.urlName = urlName;
        this.organisationType = organisationType;
    }

    public String getName() {
        return name;
    }

    public String getUrlName() {
        return urlName;
    }

    public OrganisationType getOrganisationType() {
        return organisationType;
    }
}
