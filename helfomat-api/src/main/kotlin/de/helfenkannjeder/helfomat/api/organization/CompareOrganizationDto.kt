package de.helfenkannjeder.helfomat.api.organization

data class CompareOrganizationDto(
    val original: OrganizationDetailDto?,
    val updated: OrganizationDetailDto
)