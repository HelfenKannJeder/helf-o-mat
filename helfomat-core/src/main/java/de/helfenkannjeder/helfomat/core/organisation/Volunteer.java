package de.helfenkannjeder.helfomat.core.organisation;

import de.helfenkannjeder.helfomat.core.picture.PictureId;

/**
 * @author Valentin Zickner
 */
public class Volunteer {
    private String firstname;
    private String lastname;
    private String motivation;
    private PictureId picture;

    private Volunteer() {
    }

    private Volunteer(String firstname, String lastname, String motivation, PictureId picture) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.motivation = motivation;
        this.picture = picture;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getMotivation() {
        return motivation;
    }

    public PictureId getPicture() {
        return picture;
    }

    public static class Builder {
        private String firstname;
        private String lastname;
        private String motivation;
        private PictureId picture;

        public Builder setFirstname(String firstname) {
            this.firstname = firstname;
            return this;
        }

        public Builder setLastname(String lastname) {
            this.lastname = lastname;
            return this;
        }

        public Builder setMotivation(String motivation) {
            this.motivation = motivation;
            return this;
        }

        public Builder setPicture(PictureId picture) {
            this.picture = picture;
            return this;
        }

        public Volunteer build() {
            return new Volunteer(firstname, lastname, motivation, picture);
        }
    }
}
