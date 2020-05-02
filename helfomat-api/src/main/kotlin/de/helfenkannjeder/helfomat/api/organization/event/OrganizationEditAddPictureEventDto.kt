package de.helfenkannjeder.helfomat.api.organization.event;

import de.helfenkannjeder.helfomat.core.organization.OrganizationId;
import de.helfenkannjeder.helfomat.core.picture.PictureId;

/**
 * @author Valentin Zickner
 */
public class OrganizationEditAddPictureEventDto extends OrganizationEventDto {
    private int index;
    private PictureId pictureId;

    OrganizationEditAddPictureEventDto() {
    }

    public OrganizationEditAddPictureEventDto(OrganizationId organizationId, int index, PictureId pictureId) {
        super(organizationId);
        this.index = index;
        this.pictureId = pictureId;
    }

    public int getIndex() {
        return index;
    }

    public PictureId getPictureId() {
        return pictureId;
    }

    @Override
    public <T> T visit(OrganizationEventDtoVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
