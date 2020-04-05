package de.helfenkannjeder.helfomat.api.template;

import de.helfenkannjeder.helfomat.core.template.GroupTemplate;
import de.helfenkannjeder.helfomat.core.template.OrganizationTemplate;

import java.util.List;
import java.util.stream.Collectors;

public class OrganizationTemplateAssembler {

    public static OrganizationTemplateDto toOrganizationTemplateDto(OrganizationTemplate organizationTemplate) {
        return new OrganizationTemplateDto(
            organizationTemplate.getName(),
            toGroupTemplates(organizationTemplate.getGroups())
        );
    }

    private static List<GroupTemplateDto> toGroupTemplates(List<GroupTemplate> groups) {
        return groups.stream()
            .map(OrganizationTemplateAssembler::toGroupTemplate)
            .collect(Collectors.toList());
    }

    private static GroupTemplateDto toGroupTemplate(GroupTemplate groupTemplate) {
        return new GroupTemplateDto(
            groupTemplate.getName(),
            groupTemplate.getDescription()
        );
    }

}
