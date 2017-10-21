package de.helfekannjeder.helfomat.core.organisation;

public enum OrganisationType {
    THW("Technisches Hilfswerk"),
    Aktivbuero("Aktivbüro"),
    Sonstige("Sonstige"),
    Helfenkannjeder("HelfenKannJeder");

    String name;

    OrganisationType(String name){
        this.name = name;
    }

    public String toString() {
        return name;
    }
}
