package de.helfenkannjeder.helfomat.core.organization.event;

import de.helfenkannjeder.helfomat.core.organization.OrganizationId;

/**
 * @author Valentin Zickner
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
public abstract class OrganizationEditEvent extends OrganizationEvent {

    protected OrganizationEditEvent() {
    }

    public OrganizationEditEvent(OrganizationId organizationId) {
        super(organizationId);
    }

}
