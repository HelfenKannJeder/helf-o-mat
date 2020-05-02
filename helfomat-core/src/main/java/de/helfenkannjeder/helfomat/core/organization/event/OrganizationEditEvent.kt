package de.helfenkannjeder.helfomat.core.organization.event

import de.helfenkannjeder.helfomat.core.organization.OrganizationId

/**
 * @author Valentin Zickner
 */
abstract class OrganizationEditEvent(organizationId: OrganizationId) : OrganizationEvent(organizationId)