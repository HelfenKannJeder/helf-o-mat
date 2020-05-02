package de.helfenkannjeder.helfomat.api.organization.event;

import de.helfenkannjeder.helfomat.core.organization.OrganizationId;
import de.helfenkannjeder.helfomat.core.picture.PictureId;

/**
 * @author Valentin Zickner
 */
public class OrganizationEditTeaserImageEventDto extends OrganizationEventDto {
    private PictureId teaserImage;

    OrganizationEditTeaserImageEventDto() {
    }

    public OrganizationEditTeaserImageEventDto(OrganizationId organizationId, PictureId teaserImage) {
        super(organizationId);
        this.teaserImage = teaserImage;
    }

    public PictureId getTeaserImage() {
        return teaserImage;
    }

    @Override
    public <T> T visit(OrganizationEventDtoVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
