package de.helfenkannjeder.helfomat.core.organisation.event;

import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;
import de.helfenkannjeder.helfomat.core.organisation.OrganizationEventVisitor;

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

    public String getWebsite() {
        return website;
    }

    @Override
    public Organisation.Builder applyOnOrganisationBuilder(Organisation.Builder organisation) {
        return organisation.setWebsite(website);
    }

    @Override
    public <T> T visit(OrganizationEventVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
