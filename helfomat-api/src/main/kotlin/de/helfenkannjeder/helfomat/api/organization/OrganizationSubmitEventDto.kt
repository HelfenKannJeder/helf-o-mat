package de.helfenkannjeder.helfomat.api.organization

import de.helfenkannjeder.helfomat.api.organization.event.OrganizationEventDto
import de.helfenkannjeder.helfomat.core.organization.OrganizationId

/**
 * @author Valentin Zickner
 */
data class OrganizationSubmitEventDto(
    val organizationId: OrganizationId,
    val sources: String,
    val events: List<OrganizationEventDto>
)