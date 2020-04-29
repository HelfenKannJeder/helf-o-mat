package de.helfenkannjeder.helfomat.infrastructure.config

import de.helfenkannjeder.helfomat.core.organization.OrganizationType
import de.helfenkannjeder.helfomat.core.template.GroupTemplate
import de.helfenkannjeder.helfomat.core.template.OrganizationTemplate
import de.helfenkannjeder.helfomat.core.template.OrganizationTemplateRepository
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Repository
import java.util.stream.Collectors

/**
 * @author Valentin Zickner
 */
@Repository
@EnableConfigurationProperties(OrganizationTemplateConfiguration::class)
class ConfigOrganizationTemplateRepository(organizationTemplateConfiguration: OrganizationTemplateConfiguration) : OrganizationTemplateRepository {

    private val organizationTemplates = organizationTemplateConfiguration.organizationTemplates
        .associateBy { it.name }

    override fun findByOrganizationType(organizationType: OrganizationType): OrganizationTemplate? {
        val template = organizationTemplates[organizationType.getName()] ?: return null
        return OrganizationTemplate(
            template.name,
            template.acronym,
            true,
            toGroupTemplates(template.groups)
        )
    }

    private fun toGroupTemplates(groups: List<OrganizationTemplateConfiguration.GroupTemplate>): List<GroupTemplate> {
        return groups
            .stream()
            .map { groupTemplate: OrganizationTemplateConfiguration.GroupTemplate -> toGroupTemplate(groupTemplate) }
            .collect(Collectors.toList())
    }

    private fun toGroupTemplate(groupTemplate: OrganizationTemplateConfiguration.GroupTemplate): GroupTemplate {
        return GroupTemplate(groupTemplate.name, groupTemplate.description)
    }

}