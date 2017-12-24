package de.helfenkannjeder.helfomat.core.organisation.event;

import de.helfenkannjeder.helfomat.core.event.DomainEvent;
import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;

/**
 * @author Valentin Zickner
 */
public abstract class OrganisationEvent extends DomainEvent {
    private OrganisationId organisationId;

    public OrganisationEvent(OrganisationId organisationId) {
        this.organisationId = organisationId;
    }

    public OrganisationId getOrganisationId() {
        return organisationId;
    }

    public abstract Organisation.Builder applyOnOrganisationBuilder(Organisation.Builder organisation);

}
