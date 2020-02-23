package de.helfenkannjeder.helfomat.api.organisation.event;

import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;
import de.helfenkannjeder.helfomat.core.picture.PictureId;

/**
 * @author Valentin Zickner
 */
public class OrganizationEditDeletePictureEventDto extends OrganizationEventDto {
    private PictureId pictureId;

    OrganizationEditDeletePictureEventDto() {
    }

    public OrganizationEditDeletePictureEventDto(OrganisationId organisationId, PictureId pictureId) {
        super(organisationId);
        this.pictureId = pictureId;
    }

    public PictureId getPictureId() {
        return pictureId;
    }
}
