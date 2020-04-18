package de.helfenkannjeder.helfomat.infrastructure.config;

import de.helfenkannjeder.helfomat.core.organization.OrganizationType;
import de.helfenkannjeder.helfomat.core.template.GroupTemplate;
import de.helfenkannjeder.helfomat.core.template.OrganizationTemplate;
import de.helfenkannjeder.helfomat.core.template.OrganizationTemplateRepository;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Valentin Zickner
 */
@Repository
@EnableConfigurationProperties(OrganizationTemplateConfiguration.class)
public class ConfigOrganizationTemplateRepository implements OrganizationTemplateRepository {

    private final Map<String, OrganizationTemplateConfiguration.Template> organizationTemplates;

    public ConfigOrganizationTemplateRepository(OrganizationTemplateConfiguration organizationTemplateConfiguration) {
        organizationTemplates = organizationTemplateConfiguration.getOrganizationTemplates()
            .stream()
            .collect(Collectors.toMap(OrganizationTemplateConfiguration.Template::getName, Function.identity()));
    }

    @Override
    public OrganizationTemplate findByOrganizationType(OrganizationType organizationType) {
        OrganizationTemplateConfiguration.Template template = organizationTemplates.get(organizationType.getName());
        return new OrganizationTemplate(
            template.getName(),
            template.getAcronym(),
            true,
            toGroupTemplates(template.getGroups())
        );
    }

    private static List<GroupTemplate> toGroupTemplates(List<OrganizationTemplateConfiguration.GroupTemplate> groups) {
        return groups
            .stream()
            .map(ConfigOrganizationTemplateRepository::toGroupTemplate)
            .collect(Collectors.toList());
    }

    private static GroupTemplate toGroupTemplate(OrganizationTemplateConfiguration.GroupTemplate groupTemplate) {
        return new GroupTemplate(groupTemplate.getName(), groupTemplate.getDescription());
    }

}
