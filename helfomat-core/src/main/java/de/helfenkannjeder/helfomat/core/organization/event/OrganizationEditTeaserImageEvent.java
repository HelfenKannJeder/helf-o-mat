package de.helfenkannjeder.helfomat.core.organization.event;

import de.helfenkannjeder.helfomat.core.organization.Organization;
import de.helfenkannjeder.helfomat.core.organization.OrganizationEventVisitor;
import de.helfenkannjeder.helfomat.core.organization.OrganizationId;
import de.helfenkannjeder.helfomat.core.picture.PictureId;

/**
 * @author Valentin Zickner
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
public class OrganizationEditTeaserImageEvent extends OrganizationEditEvent {
    private PictureId teaserImage;

    protected OrganizationEditTeaserImageEvent() {
    }

    public OrganizationEditTeaserImageEvent(OrganizationId organizationId, PictureId teaserImage) {
        super(organizationId);
        this.teaserImage = teaserImage;
    }

    public PictureId getTeaserImage() {
        return teaserImage;
    }

    @Override
    public Organization.Builder applyOnOrganizationBuilder(Organization.Builder organization) {
        return organization.setTeaserImage(teaserImage);
    }

    @Override
    public <T> T visit(OrganizationEventVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return "OrganizationEditTeaserImageEvent{" +
            "teaserImage=" + teaserImage +
            '}';
    }
}
