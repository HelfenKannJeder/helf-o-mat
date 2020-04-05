package de.helfenkannjeder.helfomat.api.organisation.event;

import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;
import de.helfenkannjeder.helfomat.core.picture.PictureId;

/**
 * @author Valentin Zickner
 */
public class OrganizationEditAddPictureEventDto extends OrganizationEventDto {
    private int index;
    private PictureId pictureId;

    OrganizationEditAddPictureEventDto() {
    }

    public OrganizationEditAddPictureEventDto(OrganisationId organisationId, int index, PictureId pictureId) {
        super(organisationId);
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
