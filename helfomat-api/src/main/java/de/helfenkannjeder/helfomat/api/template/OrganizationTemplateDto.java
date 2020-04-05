package de.helfenkannjeder.helfomat.api.template;

import java.util.List;

public class OrganizationTemplateDto {
    private String name;
    private List<GroupTemplateDto> groups;

    public OrganizationTemplateDto(String name, List<GroupTemplateDto> groups) {
        this.name = name;
        this.groups = groups;
    }

    public String getName() {
        return name;
    }

    public List<GroupTemplateDto> getGroups() {
        return groups;
    }
}
