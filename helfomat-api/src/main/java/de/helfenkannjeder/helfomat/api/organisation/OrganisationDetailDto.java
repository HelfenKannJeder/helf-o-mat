package de.helfenkannjeder.helfomat.api.organisation;

import de.helfenkannjeder.helfomat.api.question.AnsweredQuestionDto;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationType;
import de.helfenkannjeder.helfomat.core.picture.PictureId;

import java.util.List;

/**
 * @author Valentin Zickner
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal", "unused"})
public class OrganisationDetailDto {

    private String id;
    private String name;
    private String urlName;
    private OrganisationType organizationType;
    private String description;
    private String website;
    private PictureId logo;
    private List<PictureId> pictures;
    private List<ContactPersonDto> contactPersons;
    private AddressDto defaultAddress;
    private List<AddressDto> addresses;
    private List<AnsweredQuestionDto> questions;
    private String mapPin;
    private List<GroupDto> groups;
    private List<AttendanceTimeDto> attendanceTimes;
    private List<VolunteerDto> volunteers;

    private OrganisationDetailDto() {
    }

    public OrganisationDetailDto(
        String id,
        String name,
        String urlName,
        OrganisationType organizationType, String description,
        String website,
        PictureId logo,
        List<PictureId> pictures,
        List<ContactPersonDto> contactPersons,
        AddressDto defaultAddress,
        List<AddressDto> addresses,
        List<AnsweredQuestionDto> questions,
        String mapPin, List<GroupDto> groups,
        List<AttendanceTimeDto> attendanceTimes,
        List<VolunteerDto> volunteers
    ) {
        this.id = id;
        this.name = name;
        this.urlName = urlName;
        this.organizationType = organizationType;
        this.description = description;
        this.website = website;
        this.logo = logo;
        this.pictures = pictures;
        this.contactPersons = contactPersons;
        this.defaultAddress = defaultAddress;
        this.addresses = addresses;
        this.questions = questions;
        this.mapPin = mapPin;
        this.groups = groups;
        this.attendanceTimes = attendanceTimes;
        this.volunteers = volunteers;
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

    public OrganisationType getOrganizationType() {
        return organizationType;
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

    public List<ContactPersonDto> getContactPersons() {
        return contactPersons;
    }

    public AddressDto getDefaultAddress() {
        return defaultAddress;
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

    public List<AttendanceTimeDto> getAttendanceTimes() {
        return attendanceTimes;
    }

    public List<VolunteerDto> getVolunteers() {
        return volunteers;
    }
}
