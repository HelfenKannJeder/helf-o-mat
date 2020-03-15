package de.helfenkannjeder.helfomat.api.organisation;

import com.google.common.base.Objects;
import de.helfenkannjeder.helfomat.api.question.AnsweredQuestionDto;
import de.helfenkannjeder.helfomat.core.organisation.Address;
import de.helfenkannjeder.helfomat.core.organisation.AttendanceTime;
import de.helfenkannjeder.helfomat.core.organisation.ContactPerson;
import de.helfenkannjeder.helfomat.core.organisation.Group;
import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;
import de.helfenkannjeder.helfomat.core.organisation.QuestionAnswer;
import de.helfenkannjeder.helfomat.core.organisation.ScoredOrganisation;
import de.helfenkannjeder.helfomat.core.organisation.Volunteer;
import de.helfenkannjeder.helfomat.core.picture.PictureId;
import de.helfenkannjeder.helfomat.core.question.Question;
import de.helfenkannjeder.helfomat.core.question.QuestionId;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Valentin Zickner
 */
public class OrganisationAssembler {

    static List<OrganisationDto> toScoredOrganisationDtos(List<ScoredOrganisation> organisations) {
        return organisations
            .stream()
            .map(OrganisationAssembler::toOrganisationDto)
            .collect(Collectors.toList());
    }

    private static OrganisationDto toOrganisationDto(ScoredOrganisation scoredOrganisation) {
        Organisation organisation = scoredOrganisation.getOrganisation();
        return new OrganisationDto(
            organisation.getId().getValue(),
            organisation.getName(),
            organisation.getUrlName(),
            organisation.getDescription(),
            organisation.getWebsite(),
            organisation.getMapPin(),
            toAddressDtos(organisation.getAddresses()),
            toContactPersonDtos(organisation.getContactPersons()),
            organisation.getLogo(),
            scoredOrganisation.getScore()
        );
    }

    static List<OrganisationDto> toOrganisationDtos(List<Organisation> organisations) {
        return organisations
            .stream()
            .map(OrganisationAssembler::toOrganisationDto)
            .collect(Collectors.toList());
    }

    private static OrganisationDto toOrganisationDto(Organisation organisation) {
        return new OrganisationDto(
            organisation.getId().getValue(),
            organisation.getName(),
            organisation.getUrlName(),
            organisation.getDescription(),
            organisation.getWebsite(),
            organisation.getMapPin(),
            toAddressDtos(organisation.getAddresses()),
            toContactPersonDtos(organisation.getContactPersons()),
            organisation.getLogo(),
            null
        );
    }

    static OrganisationDetailDto toOrganisationDetailDto(Organisation organisation, List<Question> questions) {
        if (organisation == null) {
            return null;
        }
        return new OrganisationDetailDto(
            organisation.getId().getValue(),
            organisation.getName(),
            organisation.getUrlName(),
            organisation.getOrganisationType(),
            organisation.getDescription(),
            organisation.getWebsite(),
            organisation.getLogo(),
            toPictures(organisation.getPictures()),
            toContactPersonDtos(organisation.getContactPersons()),
            toAddressDto(organisation.getDefaultAddress()),
            toAddressDtos(organisation.getAddresses()),
            toAnsweredQuestionDtos(organisation.getQuestionAnswers(), questions),
            organisation.getMapPin(),
            toGroupDtos(organisation.getGroups()),
            toAttendanceTimeDtos(organisation.getAttendanceTimes()),
            toVolunteerDtos(organisation.getVolunteers())
        );
    }

    private static List<VolunteerDto> toVolunteerDtos(List<Volunteer> volunteers) {
        if (volunteers == null) {
            return Collections.emptyList();
        }
        return volunteers
            .stream()
            .map(OrganisationAssembler::toVolunteerDto)
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
            .map(OrganisationAssembler::toAttendanceTimeDto)
            .collect(Collectors.toList());
    }

    public static AttendanceTimeDto toAttendanceTimeDto(AttendanceTime attendanceTime) {
        return new AttendanceTimeDto(
            attendanceTime.getDay(),
            attendanceTime.getStart(),
            attendanceTime.getEnd(),
            attendanceTime.getNote(),
            OrganisationAssembler.toGroupDtos(attendanceTime.getGroups())
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
            .map((question) -> OrganisationAssembler.toAnsweredQuestionDto(question, determineQuestionText(questions, question.getQuestionId())))
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
        return groups.stream().map(OrganisationAssembler::toGroupDto).collect(Collectors.toList());
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
            .map(OrganisationAssembler::toContactPersonDto)
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
            .map(OrganisationAssembler::toAddressDto)
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

    public static Organisation toOrganization(OrganisationDetailDto organisationDetailDto) {
        return new Organisation.Builder()
            .setId(new OrganisationId(organisationDetailDto.getId()))
            .setName(organisationDetailDto.getName())
            .setUrlName(organisationDetailDto.getUrlName())
            .setDescription(organisationDetailDto.getDescription())
            .setWebsite(organisationDetailDto.getWebsite())
            .setLogo(organisationDetailDto.getLogo())
            .setGroups(toGroups(organisationDetailDto.getGroups()))
            .setPictures(organisationDetailDto.getPictures())
            .setDefaultAddress(toAddress(organisationDetailDto.getDefaultAddress()))
            .setAddresses(toAddresses(organisationDetailDto.getAddresses()))
            .setOrganisationType(organisationDetailDto.getOrganizationType())
            .setAttendanceTimes(toAttendanceTimes(organisationDetailDto.getAttendanceTimes()))
            .setMapPin(organisationDetailDto.getMapPin())
            .setContactPersons(toContactPersons(organisationDetailDto.getContactPersons()))
            .setQuestionAnswers(toQuestionAnswers(organisationDetailDto.getQuestions()))
            .setTeaserImage(toTeaserImage(organisationDetailDto.getPictures()))
            .setVolunteers(toVolunteers(organisationDetailDto.getVolunteers()))
            .build();
    }

    private static List<Volunteer> toVolunteers(List<VolunteerDto> volunteers) {
        if (volunteers == null) {
            return null;
        }
        return volunteers
            .stream()
            .map(OrganisationAssembler::toVolunteer)
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
            .map(OrganisationAssembler::toQuestionAnswer)
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
            .map(OrganisationAssembler::toContactPerson)
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
            .map(OrganisationAssembler::toAttendanceTime)
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
            .map(OrganisationAssembler::toGroup)
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
            .map(OrganisationAssembler::toAddress)
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
