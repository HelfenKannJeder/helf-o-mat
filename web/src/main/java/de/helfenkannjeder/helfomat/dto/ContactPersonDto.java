package de.helfenkannjeder.helfomat.dto;

/**
 * @author Valentin Zickner
 */
public class ContactPersonDto {
    private String firstname;
    private String lastname;
    private String rank;
    private String telephone;

    ContactPersonDto(String firstname, String lastname, String rank, String telephone) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.rank = rank;
        this.telephone = telephone;
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

    public static class Builder {
        private String firstname;
        private String lastname;
        private String rank;
        private String telephone;

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

        public ContactPersonDto build() {
            return new ContactPersonDto(firstname, lastname, rank, telephone);
        }
    }
}
