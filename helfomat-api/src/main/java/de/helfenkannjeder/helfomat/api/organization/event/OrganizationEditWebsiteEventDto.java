package de.helfenkannjeder.helfomat.api.organization.event;

import de.helfenkannjeder.helfomat.core.organization.OrganizationId;

/**
 * @author Valentin Zickner
 */
public class OrganizationEditWebsiteEventDto extends OrganizationEventDto {
    private String website;

    OrganizationEditWebsiteEventDto() {
    }

    public OrganizationEditWebsiteEventDto(OrganizationId organizationId, String website) {
        super(organizationId);
        this.website = website;
    }

    public String getWebsite() {
        return website;
    }

    @Override
    public <T> T visit(OrganizationEventDtoVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
