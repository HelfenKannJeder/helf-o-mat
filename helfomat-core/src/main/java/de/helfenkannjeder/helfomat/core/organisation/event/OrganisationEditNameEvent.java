package de.helfenkannjeder.helfomat.core.organisation.event;

import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;

/**
 * @author Valentin Zickner
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
public class OrganisationEditNameEvent extends OrganisationEditEvent {
    private String name;

    public OrganisationEditNameEvent(OrganisationId organisationId, String name) {
        super(organisationId);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public Organisation.Builder applyOnOrganisationBuilder(Organisation.Builder organisation) {
        return organisation.setName(name);
    }
}
