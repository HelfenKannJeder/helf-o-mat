package de.helfenkannjeder.helfomat.core.organisation.event;

import de.helfenkannjeder.helfomat.core.organisation.Group;
import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;

/**
 * @author Valentin Zickner
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
public class OrganisationEditAddGroupEvent extends OrganisationEditEvent {
    private int index;
    private Group group;

    public OrganisationEditAddGroupEvent(OrganisationId organisationId, int index, Group group) {
        super(organisationId);
        this.index = index;
        this.group = group;
    }

    @Override
    public Organisation.Builder applyOnOrganisationBuilder(Organisation.Builder organisation) {
        return organisation.addGroup(index, group);
    }
}
