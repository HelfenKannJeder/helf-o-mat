package de.helfenkannjeder.helfomat.api.contact

import de.helfenkannjeder.helfomat.core.organization.OrganizationId

/**
 * @author Valentin Zickner
 */
data class CreateContactRequestDto(
    val captcha: String,
    val email: String,
    val subject: String,
    val message: String,
    val organizationId: OrganizationId,
    val organizationContactPersonIndex: Int
)