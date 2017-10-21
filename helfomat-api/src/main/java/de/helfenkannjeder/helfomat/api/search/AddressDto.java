package de.helfenkannjeder.helfomat.api.search;

import de.helfenkannjeder.helfomat.core.organisation.Address;

/**
 * @author Valentin Zickner
 */
public class AddressDto {
    private String street;
    private String addressAppendix;
    private String city;
    private String zipcode;
    private GeoPointDto location;
    private String telephone;
    private String website;

    public AddressDto() {
    }

    public AddressDto(String street, String addressAppendix, String city, String zipcode, GeoPointDto location, String
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

    public GeoPointDto getLocation() {
        return location;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getWebsite() {
        return website;
    }

    static AddressDto fromAddress(Address address) {
        return new AddressDto(
                address.getStreet(),
                address.getAddressAppendix(),
                address.getCity(),
                address.getZipcode(),
                GeoPointDto.fromGeoPoint(address.getLocation()),
                address.getTelephone(),
                address.getWebsite()
        );
    }
}
