package de.helfenkannjeder.helfomat.infrastructure.config

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * @author Valentin Zickner
 */
@ConfigurationProperties("helfomat")
data class OrganizationTemplateConfiguration(
    var organizationTemplates: List<Template> = ArrayList()
) {

    class Template (
        var name: String,
        var acronym: String,
        var groups: List<GroupTemplate> = ArrayList()
    )

    class GroupTemplate (
        var name: String,
        var description: String
    )

}