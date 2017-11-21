package de.helfenkannjeder.helfomat.core.organisation;

import de.helfenkannjeder.helfomat.core.picture.PictureId;

/**
 * @author Valentin Zickner
 */
public class ContactPerson {
    private String firstname;
    private String lastname;
    private String rank;
    private String telephone;
    private String mail;
    private PictureId picture;

    ContactPerson() {
    }

    ContactPerson(String firstname, String lastname, String rank, String telephone, String mail, PictureId picture) {
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

    public String getLastname() {
        return lastname;
    }

    public String getRank() {
        return rank;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getMail() {
        return mail;
    }

    public PictureId getPicture() {
        return picture;
    }

    public static class Builder {
        private String firstname;
        private String lastname;
        private String rank;
        private String telephone;
        private String mail;
        private PictureId picture;

        public Builder setFirstname(String firstname) {
            this.firstname = firstname;
            return this;
        }

        public Builder setLastname(String lastname) {
            this.lastname = lastname;
            return this;
        }

        public Builder setRank(String rank) {
            this.rank = rank;
            return this;
        }

        public Builder setTelephone(String telephone) {
            this.telephone = telephone;
            return this;
        }

        public Builder setMail(String mail) {
            this.mail = mail;
            return this;
        }

        public Builder setPicture(PictureId picture) {
            this.picture = picture;
            return this;
        }

        public ContactPerson build() {
            return new ContactPerson(firstname, lastname, rank, telephone, mail, picture);
        }
    }
}
