package de.helfenkannjeder.helfomat.core.organization.event

import de.helfenkannjeder.helfomat.core.organization.Group
import de.helfenkannjeder.helfomat.core.organization.OrganizationId

abstract class OrganizationEditGroupEvent(
    override val organizationId: OrganizationId,
    open val group: Group
) : OrganizationEditEvent(organizationId)