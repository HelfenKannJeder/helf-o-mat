package de.helfenkannjeder.helfomat.api.organisation.event;

import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;
import de.helfenkannjeder.helfomat.core.picture.PictureId;

/**
 * @author Valentin Zickner
 */
public class OrganizationEditLogoEventDto extends OrganizationEventDto {
    private PictureId logo;

    OrganizationEditLogoEventDto() {
    }

    public OrganizationEditLogoEventDto(OrganisationId organisationId, PictureId logo) {
        super(organisationId);
        this.logo = logo;
    }

    public PictureId getLogo() {
        return logo;
    }
}
