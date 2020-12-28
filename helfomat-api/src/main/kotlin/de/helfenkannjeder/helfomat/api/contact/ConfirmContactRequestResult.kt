package de.helfenkannjeder.helfomat.api.contact

import de.helfenkannjeder.helfomat.core.organization.OrganizationId

/**
 * @author Valentin Zickner
 */
data class ConfirmContactRequestResult(
    val organizationId: OrganizationId,
    val organizationUrl: String
)