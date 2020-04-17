package de.helfenkannjeder.helfomat.api.organization;

import com.google.common.base.Objects;
import de.helfenkannjeder.helfomat.api.question.AnsweredQuestionDto;
import de.helfenkannjeder.helfomat.core.organization.Address;
import de.helfenkannjeder.helfomat.core.organization.AttendanceTime;
import de.helfenkannjeder.helfomat.core.organization.ContactPerson;
import de.helfenkannjeder.helfomat.core.organization.Group;
import de.helfenkannjeder.helfomat.core.organization.Organization;
import de.helfenkannjeder.helfomat.core.organization.OrganizationId;
import de.helfenkannjeder.helfomat.core.organization.QuestionAnswer;
import de.helfenkannjeder.helfomat.core.organization.ScoredOrganization;
import de.helfenkannjeder.helfomat.core.organization.Volunteer;
import de.helfenkannjeder.helfomat.core.picture.PictureId;
import de.helfenkannjeder.helfomat.core.question.Question;
import de.helfenkannjeder.helfomat.core.question.QuestionId;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Valentin Zickner
 */
public class OrganizationAssembler {

    static List<OrganizationDto> toScoredOrganizationDtos(List<ScoredOrganization> organizations) {
        return organizations
            .stream()
            .map(OrganizationAssembler::toOrganizationDto)
            .collect(Collectors.toList());
    }

    private static OrganizationDto toOrganizationDto(ScoredOrganization scoredOrganization) {
        Organization organization = scoredOrganization.getOrganization();
        return new OrganizationDto(
            organization.getId().getValue(),
            organization.getName(),
            organization.getUrlName(),
            organization.getDescription(),
            organization.getWebsite(),
            organization.getMapPin(),
            toAddressDtos(organization.getAddresses()),
            toContactPersonDtos(organization.getContactPersons()),
            organization.getLogo(),
            scoredOrganization.getScore()
        );
    }

    static List<OrganizationDto> toOrganizationDtos(List<Organization> organizations) {
        return organizations
            .stream()
            .map(OrganizationAssembler::toOrganizationDto)
            .collect(Collectors.toList());
    }

    private static OrganizationDto toOrganizationDto(Organization organization) {
        return new OrganizationDto(
            organization.getId().getValue(),
            organization.getName(),
            organization.getUrlName(),
            organization.getDescription(),
            organization.getWebsite(),
            organization.getMapPin(),
            toAddressDtos(organization.getAddresses()),
            toContactPersonDtos(organization.getContactPersons()),
            organization.getLogo(),
            null
        );
    }

    static OrganizationDetailDto toOrganizationDetailDto(Organization organization, List<Question> questions) {
        if (organization == null) {
            return null;
        }
        return new OrganizationDetailDto(
            organization.getId().getValue(),
            organization.getName(),
            organization.getUrlName(),
            organization.getOrganizationType(),
            organization.getDescription(),
            organization.getWebsite(),
            organization.getLogo(),
            toPictures(organization.getPictures()),
            toContactPersonDtos(organization.getContactPersons()),
            toAddressDto(organization.getDefaultAddress()),
            toAddressDtos(organization.getAddresses()),
            toAnsweredQuestionDtos(organization.getQuestionAnswers(), questions),
            organization.getMapPin(),
            toGroupDtos(organization.getGroups()),
            toAttendanceTimeDtos(organization.getAttendanceTimes()),
            toVolunteerDtos(organization.getVolunteers())
        );
    }

    private static List<VolunteerDto> toVolunteerDtos(List<Volunteer> volunteers) {
        if (volunteers == null) {
            return Collections.emptyList();
        }
        return volunteers
            .stream()
            .map(OrganizationAssembler::toVolunteerDto)
            .collect(Collectors.toList());
    }

    public static VolunteerDto toVolunteerDto(Volunteer volunteer) {
        return new VolunteerDto(
            volunteer.getFirstname(),
            volunteer.getMotivation(),
            volunteer.getPicture()
        );
    }

    private static List<AttendanceTimeDto> toAttendanceTimeDtos(List<AttendanceTime> attendanceTimes) {
        if (attendanceTimes == null) {
            return Collections.emptyList();
        }
        return attendanceTimes
            .stream()
            .map(OrganizationAssembler::toAttendanceTimeDto)
            .collect(Collectors.toList());
    }

    public static AttendanceTimeDto toAttendanceTimeDto(AttendanceTime attendanceTime) {
        return new AttendanceTimeDto(
            attendanceTime.getDay(),
            attendanceTime.getStart(),
            attendanceTime.getEnd(),
            attendanceTime.getNote(),
            OrganizationAssembler.toGroupDtos(attendanceTime.getGroups())
        );
    }

    private static List<PictureId> toPictures(List<PictureId> pictures) {
        if (pictures == null) {
            return Collections.emptyList();
        }
        return pictures;
    }

    private static List<AnsweredQuestionDto> toAnsweredQuestionDtos(List<QuestionAnswer> questionAnswers, List<Question> questions) {
        if (questionAnswers == null) {
            return Collections.emptyList();
        }
        return questionAnswers.stream()
            .map((question) -> OrganizationAssembler.toAnsweredQuestionDto(question, determineQuestionText(questions, question.getQuestionId())))
            .collect(Collectors.toList());
    }

    public static AnsweredQuestionDto toAnsweredQuestionDto(QuestionAnswer questionAnswer, String question) {
        return new AnsweredQuestionDto(
            questionAnswer.getQuestionId(),
            question,
            questionAnswer.getAnswer()
        );
    }

    public static String determineQuestionText(List<Question> questions, QuestionId id) {
        return questions.stream()
            .filter(question -> Objects.equal(question.getId(), id))
            .map(Question::getQuestion)
            .findFirst()
            .orElse(null);
    }

    private static List<GroupDto> toGroupDtos(List<Group> groups) {
        if (groups == null) {
            return Collections.emptyList();
        }
        return groups.stream().map(OrganizationAssembler::toGroupDto).collect(Collectors.toList());
    }

    public static GroupDto toGroupDto(Group group) {
        return new GroupDto(
            group.getName(),
            group.getDescription()
        );
    }

    private static List<ContactPersonDto> toContactPersonDtos(List<ContactPerson> contactPersons) {
        if (contactPersons == null) {
            return Collections.emptyList();
        }
        return contactPersons
            .stream()
            .map(OrganizationAssembler::toContactPersonDto)
            .collect(Collectors.toList());
    }

    public static ContactPersonDto toContactPersonDto(ContactPerson contactPerson) {
        return new ContactPersonDto(
            contactPerson.getFirstname(),
            contactPerson.getLastname(),
            contactPerson.getRank(),
            contactPerson.getTelephone(),
            contactPerson.getMail(),
            contactPerson.getPicture()
        );
    }

    private static List<AddressDto> toAddressDtos(List<Address> addresses) {
        if (addresses == null) {
            return Collections.emptyList();
        }
        return addresses
            .stream()
            .map(OrganizationAssembler::toAddressDto)
            .collect(Collectors.toList());
    }

    public static AddressDto toAddressDto(Address address) {
        if (address == null) {
            return null;
        }
        return new AddressDto(
            address.getStreet(),
            address.getAddressAppendix(),
            address.getCity(),
            address.getZipcode(),
            address.getLocation(),
            address.getTelephone(),
            address.getWebsite()
        );
    }

    public static Organization toOrganization(OrganizationDetailDto organizationDetailDto) {
        return new Organization.Builder()
            .setId(new OrganizationId(organizationDetailDto.getId()))
            .setName(organizationDetailDto.getName())
            .setUrlName(organizationDetailDto.getUrlName())
            .setDescription(organizationDetailDto.getDescription())
            .setWebsite(organizationDetailDto.getWebsite())
            .setLogo(organizationDetailDto.getLogo())
            .setGroups(toGroups(organizationDetailDto.getGroups()))
            .setPictures(organizationDetailDto.getPictures())
            .setDefaultAddress(toAddress(organizationDetailDto.getDefaultAddress()))
            .setAddresses(toAddresses(organizationDetailDto.getAddresses()))
            .setOrganizationType(organizationDetailDto.getOrganizationType())
            .setAttendanceTimes(toAttendanceTimes(organizationDetailDto.getAttendanceTimes()))
            .setMapPin(organizationDetailDto.getMapPin())
            .setContactPersons(toContactPersons(organizationDetailDto.getContactPersons()))
            .setQuestionAnswers(toQuestionAnswers(organizationDetailDto.getQuestions()))
            .setTeaserImage(toTeaserImage(organizationDetailDto.getPictures()))
            .setVolunteers(toVolunteers(organizationDetailDto.getVolunteers()))
            .build();
    }

    private static List<Volunteer> toVolunteers(List<VolunteerDto> volunteers) {
        if (volunteers == null) {
            return null;
        }
        return volunteers
            .stream()
            .map(OrganizationAssembler::toVolunteer)
            .collect(Collectors.toList());
    }

    public static Volunteer toVolunteer(VolunteerDto volunteerDto) {
        return new Volunteer.Builder()
            .setFirstname(volunteerDto.getFirstname())
            .setMotivation(volunteerDto.getMotivation())
            .setPicture(volunteerDto.getPicture())
            .build();
    }

    private static PictureId toTeaserImage(List<PictureId> pictures) {
        if (pictures == null) {
            return null;
        }
        return pictures
            .stream()
            .findFirst()
            .orElse(null);
    }

    private static List<QuestionAnswer> toQuestionAnswers(List<AnsweredQuestionDto> questions) {
        if (questions == null) {
            return null;
        }
        return questions
            .stream()
            .map(OrganizationAssembler::toQuestionAnswer)
            .collect(Collectors.toList());
    }

    public static QuestionAnswer toQuestionAnswer(AnsweredQuestionDto answeredQuestionDto) {
        return new QuestionAnswer(answeredQuestionDto.getQuestionId(), answeredQuestionDto.getAnswer());
    }

    private static List<ContactPerson> toContactPersons(List<ContactPersonDto> contactPersons) {
        if (contactPersons == null) {
            return null;
        }
        return contactPersons
            .stream()
            .map(OrganizationAssembler::toContactPerson)
            .collect(Collectors.toList());
    }

    public static ContactPerson toContactPerson(ContactPersonDto contactPersonDto) {
        return new ContactPerson.Builder()
            .setFirstname(contactPersonDto.getFirstname())
            .setLastname(contactPersonDto.getLastname())
            .setPicture(contactPersonDto.getPicture())
            .setRank(contactPersonDto.getRank())
            .setTelephone(contactPersonDto.getTelephone())
            .setMail(contactPersonDto.getMail())
            .build();
    }

    private static List<AttendanceTime> toAttendanceTimes(List<AttendanceTimeDto> attendanceTimes) {
        if (attendanceTimes == null) {
            return null;
        }
        return attendanceTimes
            .stream()
            .map(OrganizationAssembler::toAttendanceTime)
            .collect(Collectors.toList());
    }

    public static AttendanceTime toAttendanceTime(AttendanceTimeDto attendanceTimeDto) {
        return new AttendanceTime.Builder()
            .setDay(attendanceTimeDto.getDay())
            .setStart(attendanceTimeDto.getStart())
            .setEnd(attendanceTimeDto.getEnd())
            .setNote(attendanceTimeDto.getNote())
            .setGroups(toGroups(attendanceTimeDto.getGroups()))
            .build();
    }

    private static List<Group> toGroups(List<GroupDto> groups) {
        if (groups == null) {
            return null;
        }
        return groups
            .stream()
            .map(OrganizationAssembler::toGroup)
            .collect(Collectors.toList());
    }

    public static Group toGroup(GroupDto groupDto) {
        return new Group.Builder()
            .setName(groupDto.getName())
            .setDescription(groupDto.getDescription())
            .build();
    }

    private static List<Address> toAddresses(List<AddressDto> addresses) {
        if (addresses == null) {
            return null;
        }
        return addresses
            .stream()
            .map(OrganizationAssembler::toAddress)
            .collect(Collectors.toList());
    }

    public static Address toAddress(AddressDto addressDto) {
        if (addressDto == null) {
            return null;
        }
        return new Address.Builder()
            .setLocation(addressDto.getLocation())
            .setCity(addressDto.getCity())
            .setStreet(addressDto.getStreet())
            .setZipcode(addressDto.getZipcode())
            .setAddressAppendix(addressDto.getAddressAppendix())
            .setTelephone(addressDto.getTelephone())
            .setWebsite(addressDto.getWebsite())
            .build();
    }

}
