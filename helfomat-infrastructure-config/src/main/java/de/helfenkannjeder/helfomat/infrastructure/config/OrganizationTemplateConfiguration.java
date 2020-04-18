package de.helfenkannjeder.helfomat.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author Valentin Zickner
 */
@ConfigurationProperties("helfomat")
public class OrganizationTemplateConfiguration {

    private List<Template> organizationTemplates;

    public List<Template> getOrganizationTemplates() {
        return organizationTemplates;
    }

    public void setOrganizationTemplates(List<Template> organizationTemplates) {
        this.organizationTemplates = organizationTemplates;
    }

    public static class Template {

        private String name;

        private String acronym;

        private List<GroupTemplate> groups;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAcronym() {
            return acronym;
        }

        public void setAcronym(String acronym) {
            this.acronym = acronym;
        }

        public List<GroupTemplate> getGroups() {
            return groups;
        }

        public void setGroups(List<GroupTemplate> groups) {
            this.groups = groups;
        }
    }

    public static class GroupTemplate {
        private String name;
        private String description;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
