package de.helfenkannjeder.helfomat.dto;

import de.helfenkannjeder.helfomat.domain.Group;

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
