package de.helfenkannjeder.helfomat.typo3.domain;

import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * @author Valentin Zickner
 */
@Entity(name = "tx_helfenkannjeder_domain_model_organisation")
public class TOrganisation {

    @Id
    private String uid;
    private String name;
    private String description;
    private String website;
    private String logo;
    private String pictures;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "organisation")
    @Where(clause="deleted=0")
    private List<TAddress> addresses;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "organisation")
    @Where(clause="deleted=0")
    private List<TGroup> groups;

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getWebsite() {
        return website;
    }

    public String getLogo() {
        return logo;
    }

    public String getPictures() {
        return pictures;
    }

    public List<TAddress> getAddresses() {
        return addresses;
    }

    public List<TGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<TGroup> groups) {
        this.groups = groups;
    }
}
