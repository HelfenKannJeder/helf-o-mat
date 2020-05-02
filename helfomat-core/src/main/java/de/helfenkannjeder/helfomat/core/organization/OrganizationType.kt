package de.helfenkannjeder.helfomat.core.organization;

import java.util.Arrays;

@SuppressWarnings("WeakerAccess")
public enum OrganizationType {
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
    BRH("Bundesverband Rettungshunde"),
    DRV("Deutscher Rettungshunde Verein");

    final String name;

    OrganizationType(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }

    public static OrganizationType findByName(String name) {
        return Arrays.stream(values())
            .filter(organizationType -> organizationType.getName().equals(name))
            .findFirst()
            .orElseThrow(() -> new IllegalOrganizationTypeException(name));
    }
}
