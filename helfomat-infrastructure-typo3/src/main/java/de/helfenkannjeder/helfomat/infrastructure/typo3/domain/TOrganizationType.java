package de.helfenkannjeder.helfomat.infrastructure.typo3.domain;

import org.hibernate.annotations.OrderBy;
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
@Entity(name = "tx_helfenkannjeder_domain_model_organisationtype")
public class TOrganizationType {

    @Id
    private String uid;

    private String name;

    private String acronym;

    private String picture;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "organisationtype")
    @Where(clause = "deleted=0")
    @OrderBy(clause = "sort")
    private List<TGroupTemplateCategory> groupTemplateCategories;

    private boolean deleted;

    private boolean hidden;
    private boolean registerable;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public List<TGroupTemplateCategory> getGroupTemplateCategories() {
        return groupTemplateCategories;
    }

    public void setGroupTemplateCategories(List<TGroupTemplateCategory> groupTemplateCategories) {
        this.groupTemplateCategories = groupTemplateCategories;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isRegisterable() {
        return registerable;
    }

    public void setRegisterable(boolean registerable) {
        this.registerable = registerable;
    }
}
