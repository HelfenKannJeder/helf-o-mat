package de.helfenkannjeder.helfomat.core.organization.event

import com.fasterxml.jackson.annotation.JsonTypeInfo
import de.helfenkannjeder.helfomat.core.event.DomainEvent
import de.helfenkannjeder.helfomat.core.organization.Organization
import de.helfenkannjeder.helfomat.core.organization.OrganizationEventVisitor
import de.helfenkannjeder.helfomat.core.organization.OrganizationId

/**
 * @author Valentin Zickner
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, property = "type")
abstract class OrganizationEvent(
    open val organizationId: OrganizationId
) : DomainEvent() {

    abstract fun applyOnOrganizationBuilder(organizationBuilder: Organization.Builder?): Organization.Builder?
    abstract fun <T> visit(visitor: OrganizationEventVisitor<T>): T

}