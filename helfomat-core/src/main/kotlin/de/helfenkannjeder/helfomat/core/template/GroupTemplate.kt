package de.helfenkannjeder.helfomat.core.template;

/**
 * @author Valentin Zickner
 */
public class GroupTemplate {

    private String name;
    private String description;

    public GroupTemplate(String name, String description) {
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
