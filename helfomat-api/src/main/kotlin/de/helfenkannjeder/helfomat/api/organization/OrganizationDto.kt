package de.helfenkannjeder.helfomat.api.organization

import de.helfenkannjeder.helfomat.core.organization.OrganizationType
import de.helfenkannjeder.helfomat.core.picture.PictureId

/**
 * @author Valentin Zickner
 */
data class OrganizationDto(
    val id: String,
    val name: String,
    val urlName: String,
    val description: String?,
    val website: String?,
    val addresses: List<AddressDto>,
    val contactPersons: List<ContactPersonDto>,
    val logo: PictureId?,
    val scoreNorm: Double?,
    val organizationType: OrganizationType
)