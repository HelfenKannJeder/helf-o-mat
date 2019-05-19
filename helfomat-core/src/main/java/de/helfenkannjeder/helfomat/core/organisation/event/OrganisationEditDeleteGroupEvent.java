package de.helfenkannjeder.helfomat.core.organisation.event;

import de.helfenkannjeder.helfomat.core.organisation.Group;
import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;

/**
 * @author Valentin Zickner
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
public class OrganisationEditDeleteGroupEvent extends OrganisationEditEvent {
    private Group group;

    protected OrganisationEditDeleteGroupEvent() {
    }

    public OrganisationEditDeleteGroupEvent(OrganisationId organisationId, Group group) {
        super(organisationId);
        this.group = group;
    }

    @Override
    public Organisation.Builder applyOnOrganisationBuilder(Organisation.Builder organisation) {
        return organisation.removeGroup(group);
    }
}
