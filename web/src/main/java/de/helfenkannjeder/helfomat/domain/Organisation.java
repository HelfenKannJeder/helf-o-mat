package de.helfenkannjeder.helfomat.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

/**
 * @author Valentin Zickner
 */
@Document(indexName = "helfomat", type = "organisation")
public class Organisation {

    @Id
    private String id;
    private String name;
    private String description;
    private String website;
    private String logo;
    private List<String> pictures;
    private List<ContactPerson> contactPersons;
    private List<Address> addresses;
    private List<Question> questions;
    private String mapPin;
    private List<Group> groups;

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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
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

    @Override
    public String toString() {
        return "Organisation{" +
            "id=" + id +
            ", name='" + name + '\'' +
            '}';
    }
}
