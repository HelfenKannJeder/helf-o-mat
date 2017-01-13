package de.helfenkannjeder.helfomat.domain;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

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
    private String logo;
    private List<String> pictures;
    private List<Address> addresses;
    private List<Question> questions;
    private String mapPin;
    private List<Group> groups;

    Organisation(String id, String name, String type, String description, String website, String logo, List<String> pictures, List<Address> addresses,
            List<Question> questions, String mapPin, List<Group> groups) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
        this.website = website;
        this.logo = logo;
        this.pictures = pictures;
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
}
