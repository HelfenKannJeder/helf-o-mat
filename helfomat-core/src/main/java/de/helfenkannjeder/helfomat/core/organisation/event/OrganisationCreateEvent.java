package de.helfenkannjeder.helfomat.core.organisation.event;

import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationType;

/**
 * @author Valentin Zickner
 */
public class OrganisationCreateEvent extends OrganisationEvent {

    private String name;
    private String urlName;
    private OrganisationType organisationType;

    public OrganisationCreateEvent(OrganisationId organisationId,
                                   String name,
                                   String urlName,
                                   OrganisationType organisationType) {
        super(organisationId);
        this.name = name;
        this.urlName = urlName;
        this.organisationType = organisationType;
    }

    @Override
    public Organisation.Builder applyOnOrganisationBuilder(Organisation.Builder organisation) {
        return new Organisation.Builder()
            .setId(getOrganisationId())
            .setName(name)
            .setUrlName(urlName)
            .setOrganisationType(organisationType);
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
