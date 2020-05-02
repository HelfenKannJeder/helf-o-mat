package de.helfenkannjeder.helfomat.api.organization;

/**
 * @author Valentin Zickner
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal", "unused"})
public class GroupDto {
    private String name;
    private String description;

    private GroupDto() {
    }

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
