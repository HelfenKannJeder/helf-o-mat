package de.helfenkannjeder.helfomat.domain;

import java.util.List;

public class OrganisationBuilder {
    private String id;
    private String name;
    private String type;
    private String description;
    private String website;
    private String logo;
    private List<ContactPerson> contactPersons;
    private List<String> pictures;
    private List<Address> addresses;
    private List<Question> questions;
    private String mapPin;
    private List<Group> groups;

    public OrganisationBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public OrganisationBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public OrganisationBuilder setType(String type) {
        this.type = type;
        return this;
    }

    public OrganisationBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public OrganisationBuilder setWebsite(String website) {
        this.website = website;
        return this;
    }

    public OrganisationBuilder setLogo(String logo) {
        this.logo = logo;
        return this;
    }

    public OrganisationBuilder setPictures(List<String> pictures) {
        this.pictures = pictures;
        return this;
    }

    public OrganisationBuilder setContactPersons(List<ContactPerson> contactPersons) {
        this.contactPersons = contactPersons;
        return this;
    }

    public OrganisationBuilder setAddresses(List<Address> addresses) {
        this.addresses = addresses;
        return this;
    }

    public OrganisationBuilder setQuestions(List<Question> questions) {
        this.questions = questions;
        return this;
    }

    public OrganisationBuilder setMapPin(String mapPin) {
        this.mapPin = mapPin;
        return this;
    }

    public OrganisationBuilder setGroups(List<Group> groups) {
        this.groups = groups;
        return this;
    }

    public Organisation build() {
        return new Organisation(id, name, type, description, website, logo, pictures, contactPersons, addresses, questions, mapPin, groups);
    }
}