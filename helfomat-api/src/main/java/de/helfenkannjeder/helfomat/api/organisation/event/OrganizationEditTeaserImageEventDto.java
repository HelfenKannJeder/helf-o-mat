package de.helfenkannjeder.helfomat.api.organisation.event;

import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;
import de.helfenkannjeder.helfomat.core.picture.PictureId;

/**
 * @author Valentin Zickner
 */
public class OrganizationEditTeaserImageEventDto extends OrganizationEventDto {
    private PictureId teaserImage;

    OrganizationEditTeaserImageEventDto() {
    }

    public OrganizationEditTeaserImageEventDto(OrganisationId organisationId, PictureId teaserImage) {
        super(organisationId);
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
