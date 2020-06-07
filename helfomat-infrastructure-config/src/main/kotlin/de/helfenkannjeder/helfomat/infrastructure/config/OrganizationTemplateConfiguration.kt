package de.helfenkannjeder.helfomat.infrastructure.config

import de.helfenkannjeder.helfomat.core.picture.PictureId
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

/**
 * @author Valentin Zickner
 */
@ConstructorBinding
@ConfigurationProperties("helfomat")
data class OrganizationTemplateConfiguration(
    var organizationTemplates: List<Template> = ArrayList()
) {

    data class Template (
        var name: String,
        var acronym: String,
        var logoSuggestions: List<PictureId> = ArrayList(),
        var groups: List<GroupTemplate> = ArrayList()
    )

    data class GroupTemplate (
        var name: String,
        var description: String
    )

}