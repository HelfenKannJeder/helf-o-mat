package de.helfenkannjeder.helfomat.api.template

data class OrganizationTemplateDto(
    val name: String,
    val groups: List<GroupTemplateDto>
)