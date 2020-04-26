package de.helfenkannjeder.helfomat.core.organization.event;

import de.helfenkannjeder.helfomat.core.organization.Organization;
import de.helfenkannjeder.helfomat.core.organization.OrganizationEventVisitor;
import de.helfenkannjeder.helfomat.core.organization.OrganizationId;
import de.helfenkannjeder.helfomat.core.picture.PictureId;

/**
 * @author Valentin Zickner
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
public class OrganizationEditAddPictureEvent extends OrganizationEditEvent {
    private int index;
    private PictureId pictureId;

    protected OrganizationEditAddPictureEvent() {
    }

    public OrganizationEditAddPictureEvent(OrganizationId organizationId, int index, PictureId pictureId) {
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
    public Organization.Builder applyOnOrganizationBuilder(Organization.Builder organization) {
        return organization.addPicture(index, pictureId);
    }

    @Override
    public <T> T visit(OrganizationEventVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return "OrganizationEditAddPictureEvent{" +
            "index=" + index +
            ", pictureId=" + pictureId +
            ", organizationId=" + organizationId +
            '}';
    }
}
