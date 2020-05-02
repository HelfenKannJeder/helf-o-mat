package de.helfenkannjeder.helfomat.api.organization

import de.helfenkannjeder.helfomat.core.organization.OrganizationId

/**
 * @author Valentin Zickner
 */
class OrganizationNotFoundException(organizationId: OrganizationId) : RuntimeException("Organization with '" + organizationId.value + "' could not be found.") 