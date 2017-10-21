package de.helfekannjeder.helfomat.core.organisation;

import java.util.Objects;

/**
 * @author Valentin Zickner
 */
public class Group {
    private String name;
    private String description;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return Objects.equals(name, group.name) &&
            Objects.equals(description, group.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description);
    }
}
