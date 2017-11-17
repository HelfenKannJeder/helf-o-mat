package de.helfenkannjeder.helfomat.infrastructure.typo3.domain;

import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.List;

/**
 * @author Valentin Zickner
 */
@Entity(name = "tx_helfenkannjeder_domain_model_group")
public class TGroup {

    @Id
    private long uid;

    private String name;
    private String description;
    private int minimumAge;
    private int maximumAge;
    private String website;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "tx_helfenkannjeder_group_contactperson_mm",
        joinColumns = {@JoinColumn(name = "uid_local")},
        inverseJoinColumns = {@JoinColumn(name = "uid_foreign")}
    )
    @Where(clause = "deleted=0")
    private List<TEmployee> contactPersons;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<TEmployee> getContactPersons() {
        return contactPersons;
    }

    public void setContactPersons(List<TEmployee> contactPersons) {
        this.contactPersons = contactPersons;
    }

    public int getMinimumAge() {
        return minimumAge;
    }

    public void setMinimumAge(int minimumAge) {
        this.minimumAge = minimumAge;
    }

    public int getMaximumAge() {
        return maximumAge;
    }

    public void setMaximumAge(int maximumAge) {
        this.maximumAge = maximumAge;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
