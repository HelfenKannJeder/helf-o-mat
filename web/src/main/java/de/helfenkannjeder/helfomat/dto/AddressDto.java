package de.helfenkannjeder.helfomat.dto;

import de.helfenkannjeder.helfomat.domain.GeoPoint;

/**
 * @author Valentin Zickner
 */
public class AddressDto {
    private String street;
    private String addressAppendix;
    private String city;
    private String zipcode;
    private GeoPoint location;
    private String telephone;
    private String website;

    public AddressDto() {
    }

    public AddressDto(String street, String addressAppendix, String city, String zipcode, GeoPoint location, String
            telephone, String website) {
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
}
