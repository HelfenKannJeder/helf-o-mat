package de.helfenkannjeder.helfomat.domain;

public class AddressBuilder {
    private String street;
    private String addressAppendix;
    private String city;
    private String zipcode;
    private GeoPoint location;
    private String telephone;
    private String website;

    public AddressBuilder setStreet(String street) {
        this.street = street;
        return this;
    }

    public AddressBuilder setAddressAppendix(String addressAppendix) {
        this.addressAppendix = addressAppendix;
        return this;
    }

    public AddressBuilder setCity(String city) {
        this.city = city;
        return this;
    }

    public AddressBuilder setZipcode(String zipcode) {
        this.zipcode = zipcode;
        return this;
    }

    public AddressBuilder setLocation(GeoPoint location) {
        this.location = location;
        return this;
    }

    public AddressBuilder setTelephone(String telephone) {
        this.telephone = telephone;
        return this;
    }

    public AddressBuilder setWebsite(String website) {
        this.website = website;
        return this;
    }

    public Address build() {
        return new Address(street, addressAppendix, city, zipcode, location, telephone, website);
    }
}