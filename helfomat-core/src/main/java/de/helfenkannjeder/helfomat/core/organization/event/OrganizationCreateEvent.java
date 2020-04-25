package de.helfenkannjeder.helfomat.core.organization.event;

import de.helfenkannjeder.helfomat.core.organization.Organization;
import de.helfenkannjeder.helfomat.core.organization.OrganizationEventVisitor;
import de.helfenkannjeder.helfomat.core.organization.OrganizationId;
import de.helfenkannjeder.helfomat.core.organization.OrganizationType;

/**
 * @author Valentin Zickner
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
public class OrganizationCreateEvent extends OrganizationEvent {

    private String name;
    private String urlName;
    private OrganizationType organizationType;

    protected OrganizationCreateEvent() {
    }

    public OrganizationCreateEvent(OrganizationId organizationId,
                                   String name,
                                   String urlName,
                                   OrganizationType organizationType) {
        super(organizationId);
        this.name = name;
        this.urlName = urlName;
        this.organizationType = organizationType;
    }

    @Override
    public Organization.Builder applyOnOrganizationBuilder(Organization.Builder organizationBuilder) {
        return organizationBuilder
            .setId(getOrganizationId())
            .setName(name)
            .setUrlName(urlName)
            .setOrganizationType(organizationType);
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

    public OrganizationType getOrganizationType() {
        return organizationType;
    }

}
