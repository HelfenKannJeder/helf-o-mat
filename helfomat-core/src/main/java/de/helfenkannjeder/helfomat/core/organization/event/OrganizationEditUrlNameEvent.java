package de.helfenkannjeder.helfomat.core.organization.event;

import de.helfenkannjeder.helfomat.core.organization.Organization;
import de.helfenkannjeder.helfomat.core.organization.OrganizationEventVisitor;
import de.helfenkannjeder.helfomat.core.organization.OrganizationId;

/**
 * @author Valentin Zickner
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
public class OrganizationEditUrlNameEvent extends OrganizationEditEvent {
    private String urlName;

    protected OrganizationEditUrlNameEvent() {
    }

    public OrganizationEditUrlNameEvent(OrganizationId organizationId, String urlName) {
        super(organizationId);
        this.urlName = urlName;
    }

    public String getUrlName() {
        return urlName;
    }

    @Override
    public Organization.Builder applyOnOrganizationBuilder(Organization.Builder organization) {
        return organization.setUrlName(urlName);
    }

    @Override
    public <T> T visit(OrganizationEventVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
