package de.helfenkannjeder.helfomat.core.organisation.event;

import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;

/**
 * @author Valentin Zickner
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
public abstract class OrganisationEditEvent extends OrganisationEvent {

    protected OrganisationEditEvent() {
    }

    public OrganisationEditEvent(OrganisationId organisationId) {
        super(organisationId);
    }

}
