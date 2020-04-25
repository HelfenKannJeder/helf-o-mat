package de.helfenkannjeder.helfomat.core.organization.event;

import de.helfenkannjeder.helfomat.core.organization.Organization;
import de.helfenkannjeder.helfomat.core.organization.OrganizationEventVisitor;
import de.helfenkannjeder.helfomat.core.organization.OrganizationId;

/**
 * @author Valentin Zickner
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal", "unused"})
public class OrganizationEditDescriptionEvent extends OrganizationEditEvent {
    private String description;

    protected OrganizationEditDescriptionEvent() {
    }

    public OrganizationEditDescriptionEvent(OrganizationId organizationId, String description) {
        super(organizationId);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public Organization.Builder applyOnOrganizationBuilder(Organization.Builder organization) {
        return organization.setDescription(description);
    }

    @Override
    public <T> T visit(OrganizationEventVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return "OrganizationEditDescriptionEvent{" +
            "description='" + description + '\'' +
            '}';
    }
}
