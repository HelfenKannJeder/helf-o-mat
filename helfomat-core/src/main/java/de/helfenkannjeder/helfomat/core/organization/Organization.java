package de.helfenkannjeder.helfomat.core.organization;

import de.helfenkannjeder.helfomat.core.organization.event.OrganizationCreateEvent;
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEditAddAddressEvent;
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEditAddAttendanceTimeEvent;
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEditAddContactPersonEvent;
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEditAddGroupEvent;
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEditAddPictureEvent;
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEditAddQuestionAnswerEvent;
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEditAddVolunteerEvent;
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEditDefaultAddressEvent;
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEditDeleteAddressEvent;
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEditDeleteAttendanceTimeEvent;
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEditDeleteContactPersonEvent;
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEditDeleteGroupEvent;
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEditDeletePictureEvent;
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEditDeleteQuestionAnswerEvent;
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEditDeleteVolunteerEvent;
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEditDescriptionEvent;
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEditLogoEvent;
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEditNameEvent;
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEditTeaserImageEvent;
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEditUrlNameEvent;
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEditWebsiteEvent;
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEvent;
import de.helfenkannjeder.helfomat.core.picture.PictureId;
import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author Valentin Zickner
 */
public class Organization {

    private OrganizationId id;
    private String name;
    private String urlName;
    private OrganizationType organizationType;
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

    private Organization() {
    }

    private Organization(OrganizationId id,
                         String name,
                         String urlName,
                         OrganizationType organizationType,
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
        this.organizationType = organizationType;
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

    public OrganizationId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrlName() {
        return urlName;
    }

    public OrganizationType getOrganizationType() {
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
        return "Organization{" +
            "id=" + id +
            ", name='" + name + '\'' +
            '}';
    }

    public List<OrganizationEvent> compareTo(Organization organization) {
        List<OrganizationEvent> differences = new ArrayList<>();
        if (organization == null) {
            differences.add(new OrganizationCreateEvent(
                this.id,
                this.name,
                this.urlName,
                this.organizationType
            ));
            organization = new Organization.Builder()
                .setId(this.id)
                .setName(this.name)
                .setUrlName(this.urlName)
                .setOrganizationType(this.organizationType)
                .build();
        }

        OrganizationId id = organization.getId();
        if (!Objects.equals(organization.getName(), getName())) {
            differences.add(new OrganizationEditNameEvent(id, this.getName()));
        }
        if (!Objects.equals(organization.getUrlName(), getUrlName())) {
            differences.add(new OrganizationEditUrlNameEvent(id, this.getUrlName()));
        }
        if (!Objects.equals(organization.getDescription(), getDescription())) {
            differences.add(new OrganizationEditDescriptionEvent(id, this.getDescription()));
        }
        if (!Objects.equals(organization.getWebsite(), getWebsite())) {
            differences.add(new OrganizationEditWebsiteEvent(id, this.getWebsite()));
        }
        if (!Objects.equals(organization.getLogo(), getLogo())) {
            differences.add(new OrganizationEditLogoEvent(id, this.getLogo()));
        }
        if (!Objects.equals(organization.getTeaserImage(), getTeaserImage())) {
            differences.add(new OrganizationEditTeaserImageEvent(id, this.getTeaserImage()));
        }
        if (!Objects.equals(organization.getDefaultAddress(), getDefaultAddress())) {
            differences.add(new OrganizationEditDefaultAddressEvent(id, this.getDefaultAddress()));
        }
        if (organization.getPictures() != null && getPictures() != null) {
            getDiff(organization.getPictures(), getPictures())
                .map(result -> new OrganizationEditDeletePictureEvent(id, result.getFirst()))
                .forEach(differences::add);
            getDiff(getPictures(), organization.getPictures())
                .map(result -> new OrganizationEditAddPictureEvent(id, result.getSecond(), result.getFirst()))
                .forEach(differences::add);
        }
        if (organization.getContactPersons() != null && getContactPersons() != null) {
            getDiff(organization.getContactPersons(), getContactPersons())
                .map(result -> new OrganizationEditDeleteContactPersonEvent(id, result.getFirst()))
                .forEach(differences::add);
            getDiff(getContactPersons(), organization.getContactPersons())
                .map(result -> new OrganizationEditAddContactPersonEvent(id, result.getSecond(), result.getFirst()))
                .forEach(differences::add);
        }
        if (organization.getAddresses() != null && getAddresses() != null) {
            getDiff(organization.getAddresses(), getAddresses())
                .map(result -> new OrganizationEditDeleteAddressEvent(id, result.getFirst()))
                .forEach(differences::add);
            getDiff(getAddresses(), organization.getAddresses())
                .map(result -> new OrganizationEditAddAddressEvent(id, result.getSecond(), result.getFirst()))
                .forEach(differences::add);
        }
        if (organization.getQuestionAnswers() != null && getQuestionAnswers() != null) {
            getDiff(organization.getQuestionAnswers(), getQuestionAnswers())
                .map(result -> new OrganizationEditDeleteQuestionAnswerEvent(id, result.getFirst()))
                .forEach(differences::add);
            getDiff(getQuestionAnswers(), organization.getQuestionAnswers())
                .map(result -> new OrganizationEditAddQuestionAnswerEvent(id, result.getSecond(), result.getFirst()))
                .forEach(differences::add);
        }
        if (organization.getGroups() != null && getGroups() != null) {
            getDiff(organization.getGroups(), getGroups())
                .map(result -> new OrganizationEditDeleteGroupEvent(id, result.getFirst()))
                .forEach(differences::add);
            getDiff(getGroups(), organization.getGroups())
                .map(result -> new OrganizationEditAddGroupEvent(id, result.getSecond(), result.getFirst()))
                .forEach(differences::add);
        }
        if (organization.getAttendanceTimes() != null && getAttendanceTimes() != null) {
            getDiff(organization.getAttendanceTimes(), getAttendanceTimes())
                .map(result -> new OrganizationEditDeleteAttendanceTimeEvent(id, result.getFirst()))
                .forEach(differences::add);
            getDiff(getAttendanceTimes(), organization.getAttendanceTimes())
                .map(result -> new OrganizationEditAddAttendanceTimeEvent(id, result.getSecond(), result.getFirst()))
                .forEach(differences::add);
        }
        if (organization.getVolunteers() != null && getVolunteers() != null) {
            getDiff(organization.getVolunteers(), getVolunteers())
                .map(result -> new OrganizationEditDeleteVolunteerEvent(id, result.getFirst()))
                .forEach(differences::add);
            getDiff(getVolunteers(), organization.getVolunteers())
                .map(result -> new OrganizationEditAddVolunteerEvent(id, result.getSecond(), result.getFirst()))
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
        private OrganizationId id;
        private String name;
        private String urlName;
        private OrganizationType organizationType;
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

        public Builder(Organization organization) {
            this.id = organization.getId();
            this.name = organization.getName();
            this.urlName = organization.getUrlName();
            this.organizationType = organization.getOrganizationType();
            this.description = organization.getDescription();
            this.website = organization.getWebsite();
            this.logo = organization.getLogo();
            this.contactPersons = organization.getContactPersons();
            this.teaserImage = organization.getTeaserImage();
            this.defaultAddress = organization.getDefaultAddress();
            this.pictures = organization.getPictures();
            this.addresses = organization.getAddresses();
            this.questionAnswers = organization.getQuestionAnswers();
            this.mapPin = organization.getMapPin();
            this.groups = organization.getGroups();
            this.attendanceTimes = organization.getAttendanceTimes();
            this.volunteers = organization.getVolunteers();
        }

        public Builder setId(OrganizationId id) {
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

        public Builder setOrganizationType(OrganizationType organizationType) {
            this.organizationType = organizationType;
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

        public Organization build() {
            return new Organization(
                id,
                name,
                urlName,
                organizationType,
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
