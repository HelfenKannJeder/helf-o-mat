package de.helfenkannjeder.helfomat.infrastructure.typo3.domain;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "organisationtype")
    @NotFound(action = NotFoundAction.IGNORE)
    private TOrganisationType organisationtype;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "organisation")
    @Where(clause="deleted=0")
    private List<TAddress> addresses;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "defaultaddress")
    @Where(clause="deleted=0")
    @NotFound(action = NotFoundAction.IGNORE)
    private TAddress defaultaddress;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "organisation")
    @Where(clause = "deleted=0")
    private List<TEmployee> employees;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "organisation")
    @Where(clause="deleted=0")
    private List<TGroup> groups;

    private boolean deleted;

    private boolean hidden;

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

    public TAddress getDefaultaddress() {
        return defaultaddress;
    }

    public List<TGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<TGroup> groups) {
        this.groups = groups;
    }

    public TOrganisationType getOrganisationtype() {
        return organisationtype;
    }

    public void setOrganisationtype(TOrganisationType organisationtype) {
        this.organisationtype = organisationtype;
    }

    public List<TEmployee> getEmployees() {
        return employees;
    }
}
