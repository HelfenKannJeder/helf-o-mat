package de.helfenkannjeder.helfomat.domain;

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
    private List<PictureId> pictures;
    private List<ContactPerson> contactPersons;
    private List<Address> addresses;
    private List<Question> questions;
    private String mapPin;
    private List<Group> groups;

    Organisation(String id,
                 String name,
                 String type,
                 String description,
                 String website,
                 PictureId logo,
                 PictureId teaserImage,
                 List<PictureId> pictures,
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

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public PictureId getLogo() {
        return logo;
    }

    public void setLogo(PictureId logo) {
        this.logo = logo;
    }

    public List<PictureId> getPictures() {
        return pictures;
    }

    public void setPictures(List<PictureId> pictures) {
        this.pictures = pictures;
    }

    @Override
    public String toString() {
        return "Organisation{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public void setMapPin(String mapPin) {
        this.mapPin = mapPin;
    }

    public String getMapPin() {
        return mapPin;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<ContactPerson> getContactPersons() {
        return contactPersons;
    }

    public void setContactPersons(List<ContactPerson> contactPersons) {
        this.contactPersons = contactPersons;
    }

    public PictureId getTeaserImage() {
        return teaserImage;
    }

    public void setTeaserImage(PictureId teaserImage) {
        this.teaserImage = teaserImage;
    }
}
