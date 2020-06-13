package de.helfenkannjeder.helfomat.api.organization

import de.helfenkannjeder.helfomat.core.organization.OrganizationId

class OrganizationConflictException(organizationId: OrganizationId, conflictOrganizationId: OrganizationId) : RuntimeException("Organization with '${organizationId.value}' conflicts with '${conflictOrganizationId.value}'.")