package de.helfenkannjeder.helfomat.api.template

import de.helfenkannjeder.helfomat.core.picture.PictureId

data class OrganizationTemplateDto(
    val name: String,
    val logoSuggestions: List<PictureId>,
    val groups: List<GroupTemplateDto>
)