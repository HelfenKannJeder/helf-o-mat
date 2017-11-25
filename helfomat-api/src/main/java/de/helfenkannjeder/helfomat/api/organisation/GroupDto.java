package de.helfenkannjeder.helfomat.api.organisation;

/**
 * @author Valentin Zickner
 */
public class GroupDto {
    private String name;
    private String description;

    GroupDto(String name, String description) {
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
