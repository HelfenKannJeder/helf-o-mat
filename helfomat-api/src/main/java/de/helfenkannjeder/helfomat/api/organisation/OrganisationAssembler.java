package de.helfenkannjeder.helfomat.api.organisation;

import com.google.common.base.Objects;
import de.helfenkannjeder.helfomat.api.question.AnsweredQuestionDto;
import de.helfenkannjeder.helfomat.core.organisation.Address;
import de.helfenkannjeder.helfomat.core.organisation.AttendanceTime;
import de.helfenkannjeder.helfomat.core.organisation.ContactPerson;
import de.helfenkannjeder.helfomat.core.organisation.Group;
import de.helfenkannjeder.helfomat.core.organisation.Organisation;
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
class OrganisationAssembler {

    static List<OrganisationDto> toScoredOrganisationDtos(List<ScoredOrganisation> organisations) {
        return organisations
            .stream()
            .map(OrganisationAssembler::toOrganisationDto)
            .collect(Collectors.toList());
    }

    static OrganisationDto toOrganisationDto(ScoredOrganisation scoredOrganisation) {
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

    static OrganisationDto toOrganisationDto(Organisation organisation) {
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
            toAttendenceTimeDtos(organisation.getAttendanceTimes()),
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

    private static VolunteerDto toVolunteerDto(Volunteer volunteer) {
        return new VolunteerDto(
            volunteer.getFirstname(),
            volunteer.getMotivation(),
            volunteer.getPicture()
        );
    }

    private static List<AttendanceTimeDto> toAttendenceTimeDtos(List<AttendanceTime> attendanceTimes) {
        if (attendanceTimes == null) {
            return Collections.emptyList();
        }
        return attendanceTimes
            .stream()
            .map(OrganisationAssembler::toAttendanceTimeDto)
            .collect(Collectors.toList());
    }

    private static AttendanceTimeDto toAttendanceTimeDto(AttendanceTime attendanceTime) {
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

    private static AnsweredQuestionDto toAnsweredQuestionDto(QuestionAnswer questionAnswer, String question) {
        return new AnsweredQuestionDto(
            questionAnswer.getQuestionId(),
            question,
            questionAnswer.getAnswer()
        );
    }

    private static String determineQuestionText(List<Question> questions, QuestionId id) {
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

    private static GroupDto toGroupDto(Group group) {
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

    private static ContactPersonDto toContactPersonDto(ContactPerson contactPerson) {
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

    private static AddressDto toAddressDto(Address address) {
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

}
