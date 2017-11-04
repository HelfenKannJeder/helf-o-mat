package de.helfenkannjeder.helfomat.core.organisation;

import de.helfenkannjeder.helfomat.core.question.Question;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

/**
 * @author Valentin Zickner
 */
@Document(indexName = "helfomat")
public class Organisation {

    @Id
    private String id;
    private String name;
    private String type;
    private String description;
    private String website;
    private PictureId logo;
    private PictureId teaserImage;
    private Address defaultAddress;
    private List<PictureId> pictures;
    private List<ContactPerson> contactPersons;
    private List<Address> addresses;
    private List<Question> questions;
    private String mapPin;
    private List<Group> groups;

    Organisation() {
    }

    Organisation(String id,
                 String name,
                 String type,
                 String description,
                 String website,
                 PictureId logo,
                 PictureId teaserImage,
                 Address defaultAddress, List<PictureId> pictures,
                 List<ContactPerson> contactPersons,
                 List<Address> addresses,
                 List<Question> questions,
                 String mapPin,
                 List<Group> groups) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
        this.website = website;
        this.logo = logo;
        this.teaserImage = teaserImage;
        this.defaultAddress = defaultAddress;
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

    public String getType() {
        return type;
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

    public List<Question> getQuestions() {
        return questions;
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

    @Override
    public String toString() {
        return "Organisation{" +
            "id=" + id +
            ", name='" + name + '\'' +
            '}';
    }

    public Address getDefaultAddress() {
        return defaultAddress;
    }

    public static class Builder {
        private String id;
        private String name;
        private String type;
        private String description;
        private String website;
        private PictureId logo;
        private List<ContactPerson> contactPersons;
        private PictureId teaserImage;
        private Address defaultAddress;
        private List<PictureId> pictures;
        private List<Address> addresses;
        private List<Question> questions;
        private String mapPin;
        private List<Group> groups;

        public Builder() {
        }

        public Builder(Organisation organisation) {
            this.id = organisation.getId();
            this.name = organisation.getName();
            this.type = organisation.getType();
            this.description = organisation.getDescription();
            this.website = organisation.getWebsite();
            this.logo = organisation.getLogo();
            this.contactPersons = organisation.getContactPersons();
            this.teaserImage = organisation.getTeaserImage();
            this.defaultAddress = organisation.getDefaultAddress();
            this.pictures = organisation.getPictures();
            this.addresses = organisation.getAddresses();
            this.questions = organisation.getQuestions();
            this.mapPin = organisation.getMapPin();
            this.groups = organisation.getGroups();
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setType(String type) {
            this.type = type;
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

        public Builder setContactPersons(List<ContactPerson> contactPersons) {
            this.contactPersons = contactPersons;
            return this;
        }

        public Builder setAddresses(List<Address> addresses) {
            this.addresses = addresses;
            return this;
        }

        public Builder setQuestions(List<Question> questions) {
            this.questions = questions;
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

        public Organisation build() {
            return new Organisation(
                id,
                name,
                type,
                description,
                website,
                logo,
                teaserImage,
                defaultAddress,
                pictures,
                contactPersons,
                addresses,
                questions,
                mapPin,
                groups);
        }
    }
}
