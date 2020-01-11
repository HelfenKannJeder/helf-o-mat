package de.helfenkannjeder.helfomat.core.organisation.event;

import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;
import de.helfenkannjeder.helfomat.core.picture.PictureId;

/**
 * @author Valentin Zickner
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
public class OrganisationEditDeletePictureEvent extends OrganisationEditEvent {
    private PictureId pictureId;

    protected OrganisationEditDeletePictureEvent() {
    }

    public OrganisationEditDeletePictureEvent(OrganisationId organisationId, PictureId pictureId) {
        super(organisationId);
        this.pictureId = pictureId;
    }

    public PictureId getPictureId() {
        return pictureId;
    }

    @Override
    public Organisation.Builder applyOnOrganisationBuilder(Organisation.Builder organisation) {
        return organisation.removePicture(pictureId);
    }
}
