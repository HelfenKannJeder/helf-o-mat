package de.helfenkannjeder.helfomat.core.organization.event;

import de.helfenkannjeder.helfomat.core.organization.Group;
import de.helfenkannjeder.helfomat.core.organization.Organization;
import de.helfenkannjeder.helfomat.core.organization.OrganizationEventVisitor;
import de.helfenkannjeder.helfomat.core.organization.OrganizationId;

/**
 * @author Valentin Zickner
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
public class OrganizationEditAddGroupEvent extends OrganizationEditEvent {
    private int index;
    private Group group;

    protected OrganizationEditAddGroupEvent() {
    }

    public OrganizationEditAddGroupEvent(OrganizationId organizationId, int index, Group group) {
        super(organizationId);
        this.index = index;
        this.group = group;
    }

    public int getIndex() {
        return index;
    }

    public Group getGroup() {
        return group;
    }

    @Override
    public Organization.Builder applyOnOrganizationBuilder(Organization.Builder organization) {
        return organization.addGroup(index, group);
    }

    @Override
    public <T> T visit(OrganizationEventVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
