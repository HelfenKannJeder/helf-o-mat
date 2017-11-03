package de.helfenkannjeder.helfomat.core.organisation;

import de.helfenkannjeder.helfomat.core.geopoint.GeoPoint;

/**
 * @author Valentin Zickner
 */
public class Address {

    private String street;
    private String addressAppendix;
    private String city;
    private String zipcode;
    private GeoPoint location;
    private String telephone;
    private String website;

    Address() {
    }

    Address(String street, String addressAppendix, String city, String zipcode, GeoPoint location, String telephone, String website) {
        this.street = street;
        this.addressAppendix = addressAppendix;
        this.city = city;
        this.zipcode = zipcode;
        this.location = location;
        this.telephone = telephone;
        this.website = website;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getAddressAppendix() {
        return addressAppendix;
    }

    public void setAddressAppendix(String addressAppendix) {
        this.addressAppendix = addressAppendix;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public static class Builder {
        private String street;
        private String addressAppendix;
        private String city;
        private String zipcode;
        private GeoPoint location;
        private String telephone;
        private String website;

        public Builder setStreet(String street) {
            this.street = street;
            return this;
        }

        public Builder setAddressAppendix(String addressAppendix) {
            this.addressAppendix = addressAppendix;
            return this;
        }

        public Builder setCity(String city) {
            this.city = city;
            return this;
        }

        public Builder setZipcode(String zipcode) {
            this.zipcode = zipcode;
            return this;
        }

        public Builder setLocation(GeoPoint location) {
            this.location = location;
            return this;
        }

        public Builder setTelephone(String telephone) {
            this.telephone = telephone;
            return this;
        }

        public Builder setWebsite(String website) {
            this.website = website;
            return this;
        }

        public Address build() {
            return new Address(street, addressAppendix, city, zipcode, location, telephone, website);
        }
    }
}
