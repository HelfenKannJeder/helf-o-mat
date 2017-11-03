package de.helfenkannjeder.helfomat.infrastructure.typo3.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author Valentin Zickner
 */
@Entity(name = "tx_helfenkannjeder_domain_model_employee")
public class TEmployee {

    @Id
    private long uid;

    private String rank;

    private String surname;
    private String prename;
    private String motivation;
    private long birthday;
    private String pictures;
    private String mail;
    private String street;
    private String city;
    private String mobilephone;
    private String teaser;
    private String headline;
    private boolean iscontact;
    private String telephone;

    public long getUid() {
        return uid;
    }

    public String getRank() {
        return rank;
    }

    public String getSurname() {
        return surname;
    }

    public String getPrename() {
        return prename;
    }

    public String getMotivation() {
        return motivation;
    }

    public long getBirthday() {
        return birthday;
    }

    public String getPictures() {
        return pictures;
    }

    public String getMail() {
        return mail;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getMobilephone() {
        return mobilephone;
    }

    public String getTeaser() {
        return teaser;
    }

    public String getHeadline() {
        return headline;
    }

    public boolean isIscontact() {
        return iscontact;
    }

    public String getTelephone() {
        return telephone;
    }
}
