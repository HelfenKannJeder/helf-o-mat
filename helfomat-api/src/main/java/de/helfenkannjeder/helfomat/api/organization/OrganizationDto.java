package de.helfenkannjeder.helfomat.api.organization;

import de.helfenkannjeder.helfomat.core.organization.OrganizationType;
import de.helfenkannjeder.helfomat.core.picture.PictureId;

import java.util.List;

/**
 * @author Valentin Zickner
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal", "unused"})
public class OrganizationDto {
    private String id;
    private String name;
    private String urlName;
    private String description;
    private String website;
    private Float scoreNorm;
    private OrganizationType organizationType;
    private List<AddressDto> addresses;
    private List<ContactPersonDto> contactPersons;
    private PictureId logo;

    private OrganizationDto() {
    }

    public OrganizationDto(String id, String name, String urlName, String description, String website, List<AddressDto> addresses,
                           List<ContactPersonDto> contactPersons, PictureId logo, Float scoreNorm, OrganizationType organizationType) {
        this.id = id;
        this.name = name;
        this.urlName = urlName;
        this.description = description;
        this.website = website;
        this.addresses = addresses;
        this.contactPersons = contactPersons;
        this.logo = logo;
        this.scoreNorm = scoreNorm;
        this.organizationType = organizationType;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrlName() {
        return urlName;
    }

    public String getDescription() {
        return description;
    }

    public String getWebsite() {
        return website;
    }

    public Float getScoreNorm() {
        return scoreNorm;
    }

    public List<AddressDto> getAddresses() {
        return addresses;
    }

    public PictureId getLogo() {
        return logo;
    }

    public List<ContactPersonDto> getContactPersons() {
        return contactPersons;
    }

    public OrganizationType getOrganizationType() {
        return organizationType;
    }
}
