package de.helfenkannjeder.helfomat.core.organization

enum class OrganizationType(val internalName: String) {
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

    override fun toString(): String {
        return internalName
    }

    companion object {
        fun findByName(name: String): OrganizationType {
            return values().filter { it.internalName == name }.firstOrNull()
                ?: throw IllegalOrganizationTypeException(name)
        }
    }

}