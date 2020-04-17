package de.helfenkannjeder.helfomat.core.organization.event;

import de.helfenkannjeder.helfomat.core.organization.Organization;
import de.helfenkannjeder.helfomat.core.organization.OrganizationEventVisitor;
import de.helfenkannjeder.helfomat.core.organization.OrganizationId;

/**
 * @author Valentin Zickner
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
public class OrganizationEditWebsiteEvent extends OrganizationEditEvent {
    private String website;

    protected OrganizationEditWebsiteEvent() {
    }

    public OrganizationEditWebsiteEvent(OrganizationId organizationId, String website) {
        super(organizationId);
        this.website = website;
    }

    public String getWebsite() {
        return website;
    }

    @Override
    public Organization.Builder applyOnOrganizationBuilder(Organization.Builder organization) {
        return organization.setWebsite(website);
    }

    @Override
    public <T> T visit(OrganizationEventVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
