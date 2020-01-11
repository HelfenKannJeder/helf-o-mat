package de.helfenkannjeder.helfomat.core.organisation;

import de.helfenkannjeder.helfomat.core.geopoint.GeoPoint;

import java.util.Objects;

/**
 * @author Valentin Zickner
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal", "unused"})
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

    public String getAddressAppendix() {
        return addressAppendix;
    }

    public String getCity() {
        return city;
    }

    public String getZipcode() {
        return zipcode;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getWebsite() {
        return website;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(street, address.street) &&
            Objects.equals(addressAppendix, address.addressAppendix) &&
            Objects.equals(city, address.city) &&
            Objects.equals(zipcode, address.zipcode) &&
            Objects.equals(location, address.location) &&
            Objects.equals(telephone, address.telephone) &&
            Objects.equals(website, address.website);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, addressAppendix, city, zipcode, location, telephone, website);
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
