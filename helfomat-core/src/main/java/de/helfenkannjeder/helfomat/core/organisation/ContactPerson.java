package de.helfenkannjeder.helfomat.core.organisation;

import de.helfenkannjeder.helfomat.core.picture.PictureId;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContactPerson that = (ContactPerson) o;
        return Objects.equals(firstname, that.firstname) &&
            Objects.equals(lastname, that.lastname) &&
            Objects.equals(rank, that.rank) &&
            Objects.equals(telephone, that.telephone) &&
            Objects.equals(mail, that.mail) &&
            Objects.equals(picture, that.picture);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstname, lastname, rank, telephone, mail, picture);
    }

    @Override
    public String toString() {
        return "ContactPerson{" +
            "firstname='" + firstname + '\'' +
            ", lastname='" + lastname + '\'' +
            ", rank='" + rank + '\'' +
            ", telephone='" + telephone + '\'' +
            ", mail='" + mail + '\'' +
            ", picture=" + picture +
            '}';
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
