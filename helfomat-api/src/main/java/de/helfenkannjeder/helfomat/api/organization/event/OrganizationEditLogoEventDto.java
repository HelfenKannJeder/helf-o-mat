package de.helfenkannjeder.helfomat.api.organization.event;

import de.helfenkannjeder.helfomat.core.organization.OrganizationId;
import de.helfenkannjeder.helfomat.core.picture.PictureId;

/**
 * @author Valentin Zickner
 */
public class OrganizationEditLogoEventDto extends OrganizationEventDto {
    private PictureId logo;

    OrganizationEditLogoEventDto() {
    }

    public OrganizationEditLogoEventDto(OrganizationId organizationId, PictureId logo) {
        super(organizationId);
        this.logo = logo;
    }

    public PictureId getLogo() {
        return logo;
    }

    @Override
    public <T> T visit(OrganizationEventDtoVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
