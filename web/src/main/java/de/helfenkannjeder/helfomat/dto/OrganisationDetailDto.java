package de.helfenkannjeder.helfomat.dto;

import de.helfenkannjeder.helfomat.domain.Organisation;
import de.helfenkannjeder.helfomat.domain.PictureId;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Valentin Zickner
 */
public class OrganisationDetailDto {

    private String id;
    private String name;
    private String description;
    private String website;
    private PictureId logo;
    private List<PictureId> pictures;
    private List<ContactPersonDto> contactPersons;
    private List<AddressDto> addresses;
    private List<AnsweredQuestionDto> questions;
    private String mapPin;
    private List<GroupDto> groups;

    public OrganisationDetailDto(String id, String name, String description, String website, PictureId logo,
                                 List<PictureId> pictures, List<ContactPersonDto> contactPersons, List<AddressDto> addresses,
                                 List<AnsweredQuestionDto> questions,
                                 String mapPin, List<GroupDto> groups) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.website = website;
        this.logo = logo;
        this.pictures = pictures;
        this.contactPersons = contactPersons;
        this.addresses = addresses;
        this.questions = questions;
        this.mapPin = mapPin;
        this.groups = groups;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getWebsite() {
        return website;
    }

    public PictureId getLogo() {
        return logo;
    }

    public List<PictureId> getPictures() {
        return pictures;
    }

    public List<AddressDto> getAddresses() {
        return addresses;
    }

    public List<AnsweredQuestionDto> getQuestions() {
        return questions;
    }

    public String getMapPin() {
        return mapPin;
    }

    public List<GroupDto> getGroups() {
        return groups;
    }

    public List<ContactPersonDto> getContactPersons() {
        return contactPersons;
    }

    public void setContactPersons(List<ContactPersonDto> contactPersons) {
        this.contactPersons = contactPersons;
    }

    public static OrganisationDetailDto fromOrganisation(Organisation organisation) {
        if (organisation == null) {
            return null;
        }
        return new OrganisationDetailDto(
            organisation.getId(),
            organisation.getName(),
            organisation.getDescription(),
            organisation.getWebsite(),
            organisation.getLogo(),
            organisation.getPictures(),
            organisation.getContactPersons().stream().map(ContactPersonDto::fromContactPerson).collect(Collectors.toList()),
            organisation.getAddresses().stream().map(AddressDto::fromAddress).collect(Collectors.toList()),
            organisation.getQuestions().stream().map(AnsweredQuestionDto::fromQuestion).collect(Collectors.toList()),
            organisation.getMapPin(),
            organisation.getGroups().stream().map(GroupDto::fromGroup).collect(Collectors.toList())
        );
    }
}
