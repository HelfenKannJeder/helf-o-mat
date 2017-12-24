package de.helfenkannjeder.helfomat.core.organisation;

import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationCreateEvent;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEditAddAddressEvent;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEditAddAttendanceTimeEvent;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEditAddContactPersonEvent;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEditAddGroupEvent;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEditAddPictureEvent;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEditAddQuestionAnswerEvent;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEditAddVolunteerEvent;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEditDefaultAddressEvent;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEditDeleteAddressEvent;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEditDeleteAttendanceTimeEvent;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEditDeleteContactPersonEvent;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEditDeleteGroupEvent;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEditDeletePictureEvent;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEditDeleteQuestionAnswerEvent;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEditDeleteVolunteerEvent;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEditDescriptionEvent;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEditLogoEvent;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEditNameEvent;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEditTeaserImageEvent;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEditUrlNameEvent;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEditWebsiteEvent;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEvent;
import de.helfenkannjeder.helfomat.core.picture.PictureId;
import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author Valentin Zickner
 */
public class Organisation {

    private OrganisationId id;
    private String name;
    private String urlName;
    private OrganisationType organisationType;
    private String description;
    private String website;
    private PictureId logo;
    private PictureId teaserImage;
    private Address defaultAddress;
    private List<PictureId> pictures;
    private List<ContactPerson> contactPersons;
    private List<Address> addresses;
    private List<QuestionAnswer> questionAnswers;
    private String mapPin;
    private List<Group> groups;
    private List<AttendanceTime> attendanceTimes;
    private List<Volunteer> volunteers;

    Organisation() {
    }

    Organisation(OrganisationId id,
                 String name,
                 String urlName,
                 OrganisationType organisationType,
                 String description,
                 String website,
                 PictureId logo,
                 PictureId teaserImage,
                 Address defaultAddress,
                 List<PictureId> pictures,
                 List<ContactPerson> contactPersons,
                 List<Address> addresses,
                 List<QuestionAnswer> questionAnswers,
                 String mapPin,
                 List<Group> groups,
                 List<AttendanceTime> attendanceTimes,
                 List<Volunteer> volunteers) {
        this.id = id;
        this.name = name;
        this.urlName = urlName;
        this.organisationType = organisationType;
        this.description = description;
        this.website = website;
        this.logo = logo;
        this.teaserImage = teaserImage;
        this.defaultAddress = defaultAddress;
        this.pictures = pictures;
        this.contactPersons = contactPersons;
        this.addresses = addresses;
        this.questionAnswers = questionAnswers;
        this.mapPin = mapPin;
        this.groups = groups;
        this.attendanceTimes = attendanceTimes;
        this.volunteers = volunteers;
    }

    public OrganisationId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrlName() {
        return urlName;
    }

    public OrganisationType getOrganisationType() {
        return organisationType;
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

    public List<Address> getAddresses() {
        return addresses;
    }

    public List<QuestionAnswer> getQuestionAnswers() {
        return questionAnswers;
    }

    public String getMapPin() {
        return mapPin;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public List<ContactPerson> getContactPersons() {
        return contactPersons;
    }

    public PictureId getTeaserImage() {
        return teaserImage;
    }

    public List<AttendanceTime> getAttendanceTimes() {
        return attendanceTimes;
    }

    public List<Volunteer> getVolunteers() {
        return volunteers;
    }

    public Address getDefaultAddress() {
        return defaultAddress;
    }

    @Override
    public String toString() {
        return "Organisation{" +
            "id=" + id +
            ", name='" + name + '\'' +
            '}';
    }

    public List<OrganisationEvent> compareTo(Organisation organisation) {
        List<OrganisationEvent> differences = new ArrayList<>();
        if (organisation == null) {
            differences.add(new OrganisationCreateEvent(
                this.id,
                this.name,
                this.urlName,
                this.organisationType
            ));
            organisation = new Organisation.Builder()
                .setId(this.id)
                .setName(this.name)
                .setUrlName(this.urlName)
                .setOrganisationType(this.organisationType)
                .build();
        }

        OrganisationId id = organisation.getId();
        if (!Objects.equals(organisation.getName(), getName())) {
            differences.add(new OrganisationEditNameEvent(id, this.getName()));
        }
        if (!Objects.equals(organisation.getUrlName(), getUrlName())) {
            differences.add(new OrganisationEditUrlNameEvent(id, this.getUrlName()));
        }
        if (!Objects.equals(organisation.getDescription(), getDescription())) {
            differences.add(new OrganisationEditDescriptionEvent(id, this.getDescription()));
        }
        if (!Objects.equals(organisation.getWebsite(), getWebsite())) {
            differences.add(new OrganisationEditWebsiteEvent(id, this.getWebsite()));
        }
        if (!Objects.equals(organisation.getLogo(), getLogo())) {
            differences.add(new OrganisationEditLogoEvent(id, this.getLogo()));
        }
        if (!Objects.equals(organisation.getTeaserImage(), getTeaserImage())) {
            differences.add(new OrganisationEditTeaserImageEvent(id, this.getTeaserImage()));
        }
        if (!Objects.equals(organisation.getDefaultAddress(), getDefaultAddress())) {
            differences.add(new OrganisationEditDefaultAddressEvent(id, this.getDefaultAddress()));
        }
        if (organisation.getPictures() != null && getPictures() != null) {
            getDiff(organisation.getPictures(), getPictures())
                .map(result -> new OrganisationEditDeletePictureEvent(id, result.getFirst()))
                .forEach(differences::add);
            getDiff(getPictures(), organisation.getPictures())
                .map(result -> new OrganisationEditAddPictureEvent(id, result.getSecond(), result.getFirst()))
                .forEach(differences::add);
        }
        if (organisation.getContactPersons() != null && getContactPersons() != null) {
            getDiff(organisation.getContactPersons(), getContactPersons())
                .map(result -> new OrganisationEditDeleteContactPersonEvent(id, result.getFirst()))
                .forEach(differences::add);
            getDiff(getContactPersons(), organisation.getContactPersons())
                .map(result -> new OrganisationEditAddContactPersonEvent(id, result.getSecond(), result.getFirst()))
                .forEach(differences::add);
        }
        if (organisation.getAddresses() != null && getAddresses() != null) {
            getDiff(organisation.getAddresses(), getAddresses())
                .map(result -> new OrganisationEditDeleteAddressEvent(id, result.getFirst()))
                .forEach(differences::add);
            getDiff(getAddresses(), organisation.getAddresses())
                .map(result -> new OrganisationEditAddAddressEvent(id, result.getSecond(), result.getFirst()))
                .forEach(differences::add);
        }
        if (organisation.getQuestionAnswers() != null && getQuestionAnswers() != null) {
            getDiff(organisation.getQuestionAnswers(), getQuestionAnswers())
                .map(result -> new OrganisationEditDeleteQuestionAnswerEvent(id, result.getFirst()))
                .forEach(differences::add);
            getDiff(getQuestionAnswers(), organisation.getQuestionAnswers())
                .map(result -> new OrganisationEditAddQuestionAnswerEvent(id, result.getSecond(), result.getFirst()))
                .forEach(differences::add);
        }
        if (organisation.getGroups() != null && getGroups() != null) {
            getDiff(organisation.getGroups(), getGroups())
                .map(result -> new OrganisationEditDeleteGroupEvent(id, result.getFirst()))
                .forEach(differences::add);
            getDiff(getGroups(), organisation.getGroups())
                .map(result -> new OrganisationEditAddGroupEvent(id, result.getSecond(), result.getFirst()))
                .forEach(differences::add);
        }
        if (organisation.getAttendanceTimes() != null && getAttendanceTimes() != null) {
            getDiff(organisation.getAttendanceTimes(), getAttendanceTimes())
                .map(result -> new OrganisationEditDeleteAttendanceTimeEvent(id, result.getFirst()))
                .forEach(differences::add);
            getDiff(getAttendanceTimes(), organisation.getAttendanceTimes())
                .map(result -> new OrganisationEditAddAttendanceTimeEvent(id, result.getSecond(), result.getFirst()))
                .forEach(differences::add);
        }
        if (organisation.getVolunteers() != null && getVolunteers() != null) {
            getDiff(organisation.getVolunteers(), getVolunteers())
                .map(result -> new OrganisationEditDeleteVolunteerEvent(id, result.getFirst()))
                .forEach(differences::add);
            getDiff(getVolunteers(), organisation.getVolunteers())
                .map(result -> new OrganisationEditAddVolunteerEvent(id, result.getSecond(), result.getFirst()))
                .forEach(differences::add);
        }
        return differences;
    }

    private static <T> Stream<Pair<T, Integer>> getDiff(List<T> list1, List<T> list2) {
        ArrayList<T> list = new ArrayList<>(list1);
        list.removeAll(list2);
        return list.stream()
            .map(item -> Pair.of(item, list1.indexOf(item)));
    }

    public static class Builder {
        private OrganisationId id;
        private String name;
        private String urlName;
        private OrganisationType organisationType;
        private String description;
        private String website;
        private PictureId logo;
        private List<ContactPerson> contactPersons = new ArrayList<>();
        private PictureId teaserImage;
        private Address defaultAddress;
        private List<PictureId> pictures = new ArrayList<>();
        private List<Address> addresses = new ArrayList<>();
        private List<QuestionAnswer> questionAnswers = new ArrayList<>();
        private String mapPin;
        private List<Group> groups = new ArrayList<>();
        private List<AttendanceTime> attendanceTimes = new ArrayList<>();
        private List<Volunteer> volunteers = new ArrayList<>();

        public Builder() {
        }

        public Builder(Organisation organisation) {
            this.id = organisation.getId();
            this.name = organisation.getName();
            this.urlName = organisation.getUrlName();
            this.organisationType = organisation.getOrganisationType();
            this.description = organisation.getDescription();
            this.website = organisation.getWebsite();
            this.logo = organisation.getLogo();
            this.contactPersons = organisation.getContactPersons();
            this.teaserImage = organisation.getTeaserImage();
            this.defaultAddress = organisation.getDefaultAddress();
            this.pictures = organisation.getPictures();
            this.addresses = organisation.getAddresses();
            this.questionAnswers = organisation.getQuestionAnswers();
            this.mapPin = organisation.getMapPin();
            this.groups = organisation.getGroups();
            this.attendanceTimes = organisation.getAttendanceTimes();
            this.volunteers = organisation.getVolunteers();
        }

        public Builder setId(OrganisationId id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setUrlName(String urlName) {
            this.urlName = urlName;
            return this;
        }

        public Builder setOrganisationType(OrganisationType organisationType) {
            this.organisationType = organisationType;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setWebsite(String website) {
            this.website = website;
            return this;
        }

        public Builder setLogo(PictureId logo) {
            this.logo = logo;
            return this;
        }

        public Builder setTeaserImage(PictureId teaserImage) {
            this.teaserImage = teaserImage;
            return this;
        }

        public Builder setDefaultAddress(Address defaultAddress) {
            this.defaultAddress = defaultAddress;
            return this;
        }

        public Builder setPictures(List<PictureId> pictures) {
            this.pictures = pictures;
            return this;
        }

        public Builder addPicture(int index, PictureId pictureId) {
            this.pictures.add(index, pictureId);
            return this;
        }

        public Builder removePicture(PictureId pictureId) {
            this.pictures.remove(pictureId);
            return this;
        }

        public Builder setContactPersons(List<ContactPerson> contactPersons) {
            this.contactPersons = contactPersons;
            return this;
        }

        public Builder addContactPerson(int index, ContactPerson contactPerson) {
            this.contactPersons.add(index, contactPerson);
            return this;
        }

        public Builder removeContactPerson(ContactPerson contactPerson) {
            this.contactPersons.remove(contactPerson);
            return this;
        }

        public Builder setAddresses(List<Address> addresses) {
            this.addresses = addresses;
            return this;
        }

        public Builder addAddress(int index, Address address) {
            this.addresses.add(index, address);
            return this;
        }

        public Builder removeAddress(Address address) {
            this.addresses.remove(address);
            return this;
        }

        public Builder setQuestionAnswers(List<QuestionAnswer> questionAnswers) {
            this.questionAnswers = questionAnswers;
            return this;
        }

        public Builder addQuestionAnswer(int index, QuestionAnswer questionAnswer) {
            this.questionAnswers.add(index, questionAnswer);
            return this;
        }

        public Builder removeQuestionAnswer(QuestionAnswer questionAnswer) {
            this.questionAnswers.remove(questionAnswer);
            return this;
        }

        public Builder setMapPin(String mapPin) {
            this.mapPin = mapPin;
            return this;
        }

        public Builder setGroups(List<Group> groups) {
            this.groups = groups;
            return this;
        }

        public Builder addGroup(int index, Group group) {
            this.groups.add(index, group);
            return this;
        }

        public Builder removeGroup(Group group) {
            this.groups.remove(group);
            return this;
        }

        public Builder setAttendanceTimes(List<AttendanceTime> attendanceTimes) {
            this.attendanceTimes = attendanceTimes;
            return this;
        }

        public Builder addAttendanceTime(int index, AttendanceTime attendanceTime) {
            this.attendanceTimes.add(index, attendanceTime);
            return this;
        }

        public Builder removeAttendanceTime(AttendanceTime attendanceTime) {
            this.attendanceTimes.remove(attendanceTime);
            return this;
        }

        public Builder setVolunteers(List<Volunteer> volunteers) {
            this.volunteers = volunteers;
            return this;
        }

        public Builder addVolunteer(int index, Volunteer volunteer) {
            this.volunteers.add(index, volunteer);
            return this;
        }

        public Builder removeVolunteer(Volunteer volunteer) {
            this.volunteers.remove(volunteer);
            return this;
        }

        public Organisation build() {
            return new Organisation(
                id,
                name,
                urlName,
                organisationType,
                description,
                website,
                logo,
                teaserImage,
                defaultAddress,
                pictures,
                contactPersons,
                addresses,
                questionAnswers,
                mapPin,
                groups,
                attendanceTimes,
                volunteers
            );
        }
    }
}
