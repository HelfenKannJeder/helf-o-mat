package de.helfenkannjeder.helfomat.api.organization.event;

import de.helfenkannjeder.helfomat.core.organization.OrganizationId;

/**
 * @author Valentin Zickner
 */
public class OrganizationEditUrlNameEventDto extends OrganizationEventDto {
    private String urlName;

    OrganizationEditUrlNameEventDto() {
    }

    public OrganizationEditUrlNameEventDto(OrganizationId organizationId, String urlName) {
        super(organizationId);
        this.urlName = urlName;
    }

    public String getUrlName() {
        return urlName;
    }

    @Override
    public <T> T visit(OrganizationEventDtoVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
