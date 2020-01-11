package de.helfenkannjeder.helfomat.core.organisation;

import java.util.List;
import java.util.Objects;

/**
 * @author Valentin Zickner
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal", "unused"})
public class Group {
    private String name;
    private String description;
    private List<ContactPerson> contactPersons;
    private Integer minimumAge;
    private Integer maximumAge;
    private String website;

    protected Group() {
    }

    private Group(String name, String description, List<ContactPerson> contactPersons, Integer minimumAge, Integer maximumAge, String website) {
        this.name = name;
        this.description = description;
        this.contactPersons = contactPersons;
        this.minimumAge = minimumAge;
        this.maximumAge = maximumAge;
        this.website = website;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<ContactPerson> getContactPersons() {
        return contactPersons;
    }

    public Integer getMinimumAge() {
        return minimumAge;
    }

    public Integer getMaximumAge() {
        return maximumAge;
    }

    public String getWebsite() {
        return website;
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

    public static class Builder {
        private String name;
        private String description;
        private List<ContactPerson> contactPersons;
        private Integer minimumAge;
        private Integer maximumAge;
        private String website;

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setContactPersons(List<ContactPerson> contactPersons) {
            this.contactPersons = contactPersons;
            return this;
        }

        public Builder setMinimumAge(Integer minimumAge) {
            this.minimumAge = minimumAge;
            return this;
        }

        public Builder setMaximumAge(Integer maximumAge) {
            this.maximumAge = maximumAge;
            return this;
        }

        public Builder setWebsite(String website) {
            this.website = website;
            return this;
        }

        public Group build() {
            return new Group(name, description, contactPersons, minimumAge, maximumAge, website);
        }
    }

}
