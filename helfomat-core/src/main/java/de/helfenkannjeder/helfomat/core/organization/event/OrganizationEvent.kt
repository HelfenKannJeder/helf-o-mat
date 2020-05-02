package de.helfenkannjeder.helfomat.core.organization.event;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.helfenkannjeder.helfomat.core.event.DomainEvent;
import de.helfenkannjeder.helfomat.core.organization.Organization;
import de.helfenkannjeder.helfomat.core.organization.OrganizationEventVisitor;
import de.helfenkannjeder.helfomat.core.organization.OrganizationId;

/**
 * @author Valentin Zickner
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
@JsonTypeInfo(
    use = JsonTypeInfo.Id.MINIMAL_CLASS,
    property = "type"
)
public abstract class OrganizationEvent extends DomainEvent {
    protected OrganizationId organizationId;

    protected OrganizationEvent() {
    }

    public OrganizationEvent(OrganizationId organizationId) {
        this.organizationId = organizationId;
    }

    public OrganizationId getOrganizationId() {
        return organizationId;
    }

    public abstract Organization.Builder applyOnOrganizationBuilder(Organization.Builder organization);

    public abstract <T> T visit(OrganizationEventVisitor<T> visitor);

}
