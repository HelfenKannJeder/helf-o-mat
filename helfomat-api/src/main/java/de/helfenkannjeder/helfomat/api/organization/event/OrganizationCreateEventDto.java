package de.helfenkannjeder.helfomat.api.organization.event;

import de.helfenkannjeder.helfomat.core.organization.OrganizationId;
import de.helfenkannjeder.helfomat.core.organization.OrganizationType;

/**
 * @author Valentin Zickner
 */
public class OrganizationCreateEventDto extends OrganizationEventDto {

    private String name;
    private String urlName;
    private OrganizationType organizationType;

    OrganizationCreateEventDto() {
    }

    public OrganizationCreateEventDto(OrganizationId organizationId, String name, String urlName, OrganizationType organizationType) {
        super(organizationId);
        this.name = name;
        this.urlName = urlName;
        this.organizationType = organizationType;
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

    @Override
    public <T> T visit(OrganizationEventDtoVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
