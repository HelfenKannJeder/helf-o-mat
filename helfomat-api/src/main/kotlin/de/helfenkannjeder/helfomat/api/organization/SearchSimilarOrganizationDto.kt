package de.helfenkannjeder.helfomat.api.organization

import de.helfenkannjeder.helfomat.core.organization.OrganizationType

/**
 * @author Valentin Zickner
 */
data class SearchSimilarOrganizationDto(
    val address: AddressDto?,
    val organizationType: OrganizationType,
    val distanceInMeters: Long
)