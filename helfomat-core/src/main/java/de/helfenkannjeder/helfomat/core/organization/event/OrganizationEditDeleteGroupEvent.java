package de.helfenkannjeder.helfomat.core.organization.event;

import de.helfenkannjeder.helfomat.core.organization.Group;
import de.helfenkannjeder.helfomat.core.organization.Organization;
import de.helfenkannjeder.helfomat.core.organization.OrganizationEventVisitor;
import de.helfenkannjeder.helfomat.core.organization.OrganizationId;

/**
 * @author Valentin Zickner
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
public class OrganizationEditDeleteGroupEvent extends OrganizationEditEvent {
    private Group group;

    protected OrganizationEditDeleteGroupEvent() {
    }

    public OrganizationEditDeleteGroupEvent(OrganizationId organizationId, Group group) {
        super(organizationId);
        this.group = group;
    }

    public Group getGroup() {
        return group;
    }

    @Override
    public Organization.Builder applyOnOrganizationBuilder(Organization.Builder organization) {
        return organization.removeGroup(group);
    }

    @Override
    public <T> T visit(OrganizationEventVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
