package de.helfenkannjeder.helfomat.core.template;

import java.util.List;

/**
 * @author Valentin Zickner
 */
public class OrganizationTemplate {

    private String name;
    private String acronym;
    private boolean eligibleForRegistration;
    private List<GroupTemplate> groups;

    public OrganizationTemplate(String name, String acronym, boolean eligibleForRegistration, List<GroupTemplate> groups) {
        this.name = name;
        this.acronym = acronym;
        this.eligibleForRegistration = eligibleForRegistration;
        this.groups = groups;
    }

    public String getName() {
        return name;
    }

    public String getAcronym() {
        return acronym;
    }

    public boolean isEligibleForRegistration() {
        return eligibleForRegistration;
    }

    public List<GroupTemplate> getGroups() {
        return groups;
    }
}
