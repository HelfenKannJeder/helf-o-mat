package de.helfenkannjeder.helfomat.api.organisation;

import de.helfenkannjeder.helfomat.api.question.AnsweredQuestionDto;
import de.helfenkannjeder.helfomat.core.organisation.Address;
import de.helfenkannjeder.helfomat.core.organisation.ContactPerson;
import de.helfenkannjeder.helfomat.core.organisation.Group;
import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.organisation.PictureId;
import de.helfenkannjeder.helfomat.core.question.Question;

import java.util.Collections;
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
            toPictures(organisation.getPictures()),
            toContactPersons(organisation.getContactPersons()),
            toAddresses(organisation.getAddresses()),
            toQuestions(organisation.getQuestions()),
            organisation.getMapPin(),
            toGroups(organisation.getGroups())
        );
    }

    private static List<PictureId> toPictures(List<PictureId> pictures) {
        if (pictures == null) {
            return Collections.emptyList();
        }
        return pictures;
    }

    private static List<ContactPersonDto> toContactPersons(List<ContactPerson> contactPersons) {
        if (contactPersons == null) {
            return Collections.emptyList();
        }
        return contactPersons.stream().map(ContactPersonDto::fromContactPerson).collect(Collectors.toList());
    }

    private static List<AddressDto> toAddresses(List<Address> addresses) {
        if (addresses == null) {
            return Collections.emptyList();
        }
        return addresses.stream().map(AddressDto::fromAddress).collect(Collectors.toList());
    }

    private static List<AnsweredQuestionDto> toQuestions(List<Question> questions) {
        if (questions == null) {
            return Collections.emptyList();
        }
        return questions.stream().map(AnsweredQuestionDto::fromQuestion).collect(Collectors.toList());
    }

    private static List<GroupDto> toGroups(List<Group> groups) {
        if (groups == null) {
            return Collections.emptyList();
        }
        return groups.stream().map(GroupDto::fromGroup).collect(Collectors.toList());
    }
}
