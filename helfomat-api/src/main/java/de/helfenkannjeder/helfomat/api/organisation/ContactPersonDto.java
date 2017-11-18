package de.helfenkannjeder.helfomat.api.organisation;

import de.helfenkannjeder.helfomat.core.picture.PictureId;

/**
 * @author Valentin Zickner
 */
public class ContactPersonDto {
    private String firstname;
    private String lastname;
    private String rank;
    private String telephone;
    private String mail;
    private PictureId picture;

    ContactPersonDto(String firstname, String lastname, String rank, String telephone, String mail, PictureId picture) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.rank = rank;
        this.telephone = telephone;
        this.mail = mail;
        this.picture = picture;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public PictureId getPicture() {
        return picture;
    }

    public void setPicture(PictureId picture) {
        this.picture = picture;
    }

}
