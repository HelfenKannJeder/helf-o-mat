package de.helfenkannjeder.helfomat.domain;

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
}
