package de.helfenkannjeder.helfomat.infrastructure.typo3.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author Valentin Zickner
 */
@Entity(name = "tx_helfenkannjeder_domain_model_organisationtype")
public class TOrganisationType {

    @Id
    private String uid;

    private String name;

    private String picture;

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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
