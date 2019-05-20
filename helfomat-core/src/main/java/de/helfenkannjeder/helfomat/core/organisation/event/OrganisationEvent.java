package de.helfenkannjeder.helfomat.core.organisation.event;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.helfenkannjeder.helfomat.core.event.DomainEvent;
import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;

/**
 * @author Valentin Zickner
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
@JsonTypeInfo(
    use = JsonTypeInfo.Id.MINIMAL_CLASS,
    property = "type"
)
public abstract class OrganisationEvent extends DomainEvent {
    private OrganisationId organisationId;

    protected OrganisationEvent() {
    }

    public OrganisationEvent(OrganisationId organisationId) {
        this.organisationId = organisationId;
    }

    public OrganisationId getOrganisationId() {
        return organisationId;
    }

    public abstract Organisation.Builder applyOnOrganisationBuilder(Organisation.Builder organisation);

}
