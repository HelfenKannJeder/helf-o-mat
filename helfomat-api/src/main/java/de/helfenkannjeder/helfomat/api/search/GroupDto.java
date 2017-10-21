package de.helfenkannjeder.helfomat.api.search;

import de.helfenkannjeder.helfomat.core.organisation.Group;

/**
 * @author Valentin Zickner
 */
public class GroupDto {
    private String name;
    private String description;

    public GroupDto() {
    }

    private GroupDto(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }


    static GroupDto fromGroup(Group group) {
        return new GroupDto(
                group.getName(),
                group.getDescription()
        );
    }
}
