package de.helfenkannjeder.helfomat.api.template

import de.helfenkannjeder.helfomat.core.template.GroupTemplate
import de.helfenkannjeder.helfomat.core.template.OrganizationTemplate

fun OrganizationTemplate.toOrganizationTemplateDto() = OrganizationTemplateDto(this.name, this.logoSuggestions, this.groups.toGroupTemplates())

fun List<GroupTemplate>.toGroupTemplates() = this.map { it.toGroupTemplate() }
fun GroupTemplate.toGroupTemplate(): GroupTemplateDto = GroupTemplateDto(this.name, this.description)