package de.helfenkannjeder.helfomat.api.organization.event;

import de.helfenkannjeder.helfomat.core.organization.OrganizationId;
import de.helfenkannjeder.helfomat.core.picture.PictureId;

/**
 * @author Valentin Zickner
 */
public class OrganizationEditDeletePictureEventDto extends OrganizationEventDto {
    private PictureId pictureId;

    OrganizationEditDeletePictureEventDto() {
    }

    public OrganizationEditDeletePictureEventDto(OrganizationId organizationId, PictureId pictureId) {
        super(organizationId);
        this.pictureId = pictureId;
    }

    public PictureId getPictureId() {
        return pictureId;
    }

    @Override
    public <T> T visit(OrganizationEventDtoVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
