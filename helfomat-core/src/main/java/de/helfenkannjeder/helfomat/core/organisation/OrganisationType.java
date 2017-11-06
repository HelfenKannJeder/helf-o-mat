package de.helfenkannjeder.helfomat.core.organisation;

import java.util.Arrays;

public enum OrganisationType {
    THW("Technisches Hilfswerk"),
    ASB("Arbeiter-Samariter-Bund"),
    BRK("Bayerisches Rotes Kreuz"),
    BW("Bergwacht"),
    DLRG("Deutsche Lebens-Rettungs-Gesellschaft"),
    DRK("Deutsches Rotes Kreuz"),
    FF("Freiwillige Feuerwehr"),
    JUH("Johanniter-Unfall-Hilfe"),
    MHD("Malteser Hilfsdienst"),
    KIT("Notfallseelsorge / Kriseninterventionsteam"),
    PRIV_SAN("Privater Sanitätsdienst"),
    BRH("Bundesverband Rettungshunde");

    String name;

    OrganisationType(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }

    public static OrganisationType findByName(String name) {
        return Arrays.stream(values())
            .filter(organisationType -> organisationType.getName().equals(name))
            .findFirst()
            .orElseThrow(() -> new IllegalOrganisationTypeException(name));
    }
}
