package de.helfenkannjeder.helfomat.core.organization.event;

import de.helfenkannjeder.helfomat.core.organization.Organization;
import de.helfenkannjeder.helfomat.core.organization.OrganizationEventVisitor;
import de.helfenkannjeder.helfomat.core.organization.OrganizationId;
import de.helfenkannjeder.helfomat.core.picture.PictureId;

/**
 * @author Valentin Zickner
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
public class OrganizationEditDeletePictureEvent extends OrganizationEditEvent {
    private PictureId pictureId;

    protected OrganizationEditDeletePictureEvent() {
    }

    public OrganizationEditDeletePictureEvent(OrganizationId organizationId, PictureId pictureId) {
        super(organizationId);
        this.pictureId = pictureId;
    }

    public PictureId getPictureId() {
        return pictureId;
    }

    @Override
    public Organization.Builder applyOnOrganizationBuilder(Organization.Builder organization) {
        return organization.removePicture(pictureId);
    }

    @Override
    public <T> T visit(OrganizationEventVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
