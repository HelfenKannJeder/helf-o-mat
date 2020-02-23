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
@Entity(name = "tx_helfenkannjeder_domain_model_grouptemplatecategory")
public class TGroupTemplateCategory {

    @Id
    private long uid;

    private String name;

    private String description;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "grouptemplatecategory")
    @Where(clause = "deleted=0")
    @OrderBy(clause = "sort")
    private List<TGroupTemplate> groupTemplates;

    public long getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<TGroupTemplate> getGroupTemplates() {
        return groupTemplates;
    }
}
