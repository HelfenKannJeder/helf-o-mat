package de.helfenkannjeder.helfomat.api.contact

import de.helfenkannjeder.helfomat.core.organization.OrganizationId

/**
 * @author Valentin Zickner
 */
data class CreateContactRequestDto(
    val captcha: String,
    val name: String,
    val email: String,
    val subject: String,
    val message: String,
    val organizationId: OrganizationId,
    val organizationContactPersonIndex: Int,

    /**
     * Anything but an empty value means the
     * submission came from some automation that filled in every input it could find.
     */
    val website: String? = null
)