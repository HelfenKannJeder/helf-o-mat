package de.helfenkannjeder.helfomat.core.organisation.event;

import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;

/**
 * @author Valentin Zickner
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
public class OrganisationEditWebsiteEvent extends OrganisationEditEvent {
    private String website;

    protected OrganisationEditWebsiteEvent() {
    }

    public OrganisationEditWebsiteEvent(OrganisationId organisationId, String website) {
        super(organisationId);
        this.website = website;
    }

    @Override
    public Organisation.Builder applyOnOrganisationBuilder(Organisation.Builder organisation) {
        return organisation.setWebsite(website);
    }
}
