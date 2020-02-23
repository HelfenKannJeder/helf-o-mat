package de.helfenkannjeder.helfomat.core.organisation.event;

import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;
import de.helfenkannjeder.helfomat.core.organisation.OrganizationEventVisitor;
import de.helfenkannjeder.helfomat.core.picture.PictureId;

/**
 * @author Valentin Zickner
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
public class OrganisationEditTeaserImageEvent extends OrganisationEditEvent {
    private PictureId teaserImage;

    protected OrganisationEditTeaserImageEvent() {
    }

    public OrganisationEditTeaserImageEvent(OrganisationId organisationId, PictureId teaserImage) {
        super(organisationId);
        this.teaserImage = teaserImage;
    }

    public PictureId getTeaserImage() {
        return teaserImage;
    }

    @Override
    public Organisation.Builder applyOnOrganisationBuilder(Organisation.Builder organisation) {
        return organisation.setTeaserImage(teaserImage);
    }

    @Override
    public <T> T visit(OrganizationEventVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
