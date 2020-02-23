package de.helfenkannjeder.helfomat.infrastructure.typo3.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author Valentin Zickner
 */
@Entity(name = "tx_helfenkannjeder_domain_model_grouptemplate")
public class TGroupTemplate {

    @Id
    private long uid;

    private String name;

    private String description;

    private String suggestion;

    public long getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getSuggestion() {
        return suggestion;
    }

}
