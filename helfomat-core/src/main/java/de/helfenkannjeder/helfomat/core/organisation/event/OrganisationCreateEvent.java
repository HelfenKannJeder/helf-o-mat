package de.helfenkannjeder.helfomat.core.organisation.event;

import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationType;
import de.helfenkannjeder.helfomat.core.organisation.OrganizationEventVisitor;

/**
 * @author Valentin Zickner
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
public class OrganisationCreateEvent extends OrganisationEvent {

    private String name;
    private String urlName;
    private OrganisationType organisationType;

    protected OrganisationCreateEvent() {
    }

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

    @Override
    public <T> T visit(OrganizationEventVisitor<T> visitor) {
        return visitor.visit(this);
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
