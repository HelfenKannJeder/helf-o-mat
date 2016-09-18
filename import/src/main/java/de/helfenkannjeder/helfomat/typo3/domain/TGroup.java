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
@Entity(name = "tx_helfenkannjeder_domain_model_group")
public class TGroup {

    @Id
    private long uid;

    private String name;
    private String description;

    @ManyToOne
    @JoinColumn(name = "template")
    @NotFound(action = NotFoundAction.IGNORE)
    @Where(clause="deleted=0")
    private TGroupTemplate template;

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

    public TGroupTemplate getTemplate() {
        return template;
    }

    public void setTemplate(TGroupTemplate template) {
        this.template = template;
    }
}
