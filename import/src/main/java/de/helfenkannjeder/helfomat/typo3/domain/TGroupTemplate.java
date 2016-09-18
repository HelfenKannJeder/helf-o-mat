package de.helfenkannjeder.helfomat.typo3.domain;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * @author Valentin Zickner
 */
@Entity(name = "tx_helfenkannjeder_domain_model_grouptemplate")
public class TGroupTemplate {

    @Id
    private Long uid;

    @ManyToOne
    @JoinColumn(name = "groupOfGroupTemplate")
    @NotFound(action = NotFoundAction.IGNORE)
    @Where(clause="deleted=0")
    private TGroupOfGroupTemplate groupOfGroupTemplate;

    public TGroupOfGroupTemplate getGroupOfGroupTemplate() {
        return groupOfGroupTemplate;
    }

    public void setGroupOfGroupTemplate(TGroupOfGroupTemplate groupOfGroupTemplate) {
        this.groupOfGroupTemplate = groupOfGroupTemplate;
    }
}
