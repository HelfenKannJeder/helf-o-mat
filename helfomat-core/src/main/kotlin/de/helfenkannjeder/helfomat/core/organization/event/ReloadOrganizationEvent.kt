package de.helfenkannjeder.helfomat.core.organization.event

import de.helfenkannjeder.helfomat.core.event.DomainEvent
import de.helfenkannjeder.helfomat.core.organization.OrganizationId

data class ReloadOrganizationEvent(
    val organizationId: OrganizationId
) : DomainEvent()