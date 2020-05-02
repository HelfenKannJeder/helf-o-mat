package de.helfenkannjeder.helfomat.api.template;

public class GroupTemplateDto {

    private String name;
    private String description;

    public GroupTemplateDto(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

}
